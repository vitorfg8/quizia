package com.vitorfg8.quizia.core.domain.usecase

import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.repository.ApiKeyRepository
import com.vitorfg8.quizia.core.domain.repository.OnDeviceModelRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HasAvailableLlmProviderUseCaseTest {

    private val mockApiKeyRepository = mockk<ApiKeyRepository>()
    private val mockOnDeviceModelRepository = mockk<OnDeviceModelRepository>()
    private val useCase = HasAvailableLlmProviderUseCase(
        apiKeyRepository = mockApiKeyRepository,
        onDeviceModelRepository = mockOnDeviceModelRepository,
    )

    @Before
    fun setUp() {
        every { mockApiKeyRepository.getApiKey(any()) } returns null
        coEvery { mockOnDeviceModelRepository.isAvailable() } returns false
    }

    @Test
    fun `on-device gemini nano counts as an available provider`() = runTest {
        coEvery { mockOnDeviceModelRepository.isAvailable() } returns true
        val actual = useCase()
        assertTrue(actual)
    }

    @Test
    fun `a stored cloud api key counts as an available provider`() = runTest {
        every { mockApiKeyRepository.getApiKey(LlmProviderType.GEMINI_API) } returns INPUT_API_KEY
        val actual = useCase()
        assertTrue(actual)
    }

    @Test
    fun `a blank stored key is ignored`() = runTest {
        every { mockApiKeyRepository.getApiKey(LlmProviderType.OPENAI) } returns "   "
        val actual = useCase()
        assertFalse(actual)
    }

    @Test
    fun `no nano and no keys means no provider is available`() = runTest {
        val actual = useCase()
        assertFalse(actual)
    }

    private companion object {
        const val INPUT_API_KEY = "sk-test"
    }
}
