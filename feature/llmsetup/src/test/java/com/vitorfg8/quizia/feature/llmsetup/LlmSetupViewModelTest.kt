package com.vitorfg8.quizia.feature.llmsetup

import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.repository.ApiKeyRepository
import com.vitorfg8.quizia.core.domain.repository.SettingsRepository
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LlmSetupViewModelTest {

    private val mockSettingsRepository = mockk<SettingsRepository>(relaxed = true)
    private val mockApiKeyRepository = mockk<ApiKeyRepository>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state hides Gemini Nano when the device does not support it`() {
        val viewModel = createViewModel(isGeminiNanoSupported = false)
        val expected = listOf(
            LlmProviderType.GEMINI_API,
            LlmProviderType.OPENAI,
            LlmProviderType.CLAUDE,
        )
        val actual = viewModel.uiState.value
        assertEquals(expected, actual.availableProviders)
        assertFalse(actual.canContinue)
    }

    @Test
    fun `initial state offers Gemini Nano when the device supports it`() {
        val viewModel = createViewModel(isGeminiNanoSupported = true)
        val actual = viewModel.uiState.value.availableProviders
        assertEquals(LlmProviderType.entries, actual)
    }

    @Test
    fun `selecting Gemini Nano hides the api key field and enables continue`() {
        val viewModel = createViewModel(isGeminiNanoSupported = true)
        viewModel.selectProvider(LlmProviderType.GEMINI_NANO)
        val actual = viewModel.uiState.value
        assertFalse(actual.isApiKeyRequired)
        assertTrue(actual.canContinue)
    }

    @Test
    fun `selecting a remote provider requires an api key before continuing`() {
        val viewModel = createViewModel()
        viewModel.selectProvider(LlmProviderType.OPENAI)
        val actual = viewModel.uiState.value
        assertTrue(actual.isApiKeyRequired)
        assertFalse(actual.canContinue)
    }

    @Test
    fun `typing an api key enables continue for a remote provider`() {
        val viewModel = createViewModel()
        viewModel.selectProvider(LlmProviderType.OPENAI)
        viewModel.updateApiKey(INPUT_API_KEY)
        val actual = viewModel.uiState.value
        assertEquals(INPUT_API_KEY, actual.apiKey)
        assertTrue(actual.canContinue)
    }

    @Test
    fun `a blank api key keeps continue disabled`() {
        val viewModel = createViewModel()
        viewModel.selectProvider(LlmProviderType.CLAUDE)
        viewModel.updateApiKey("   ")
        assertFalse(viewModel.uiState.value.canContinue)
    }

    @Test
    fun `typing an api key before choosing a provider keeps continue disabled`() {
        val viewModel = createViewModel()
        viewModel.updateApiKey(INPUT_API_KEY)
        assertFalse(viewModel.uiState.value.canContinue)
    }

    @Test
    fun `continue stores the api key and the provider then navigates home`() = runTest {
        val viewModel = createViewModel()
        viewModel.selectProvider(LlmProviderType.CLAUDE)
        viewModel.updateApiKey(INPUT_API_KEY)
        viewModel.onContinueClick()
        val actual = viewModel.sideEffect.first()
        verify { mockApiKeyRepository.saveApiKey(LlmProviderType.CLAUDE, INPUT_API_KEY) }
        coVerify { mockSettingsRepository.saveSelectedProvider(LlmProviderType.CLAUDE) }
        coVerify { mockSettingsRepository.setFirstRunComplete() }
        assertEquals(LlmSetupSideEffect.NavigateToHome, actual)
    }

    @Test
    fun `continue skips api key storage for the on-device provider`() = runTest {
        val viewModel = createViewModel(isGeminiNanoSupported = true)
        viewModel.selectProvider(LlmProviderType.GEMINI_NANO)
        viewModel.onContinueClick()
        val actual = viewModel.sideEffect.first()
        verify(exactly = 0) { mockApiKeyRepository.saveApiKey(any(), any()) }
        coVerify { mockSettingsRepository.saveSelectedProvider(LlmProviderType.GEMINI_NANO) }
        assertEquals(LlmSetupSideEffect.NavigateToHome, actual)
    }

    @Test
    fun `continue does nothing while no provider is selected`() {
        val viewModel = createViewModel()
        viewModel.onContinueClick()
        coVerify(exactly = 0) { mockSettingsRepository.saveSelectedProvider(any()) }
    }

    private fun createViewModel(isGeminiNanoSupported: Boolean = false) = LlmSetupViewModel(
        settingsRepository = mockSettingsRepository,
        apiKeyRepository = mockApiKeyRepository,
        isGeminiNanoSupported = isGeminiNanoSupported,
    )

    private companion object {
        const val INPUT_API_KEY = "test-api-key"
    }
}
