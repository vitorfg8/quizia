package com.vitorfg8.quizia.core.data.repository

import com.vitorfg8.quizia.core.data.local.ApiKeyStore
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ApiKeyRepositoryImplTest {

    private val mockApiKeyStore = mockk<ApiKeyStore>(relaxed = true)
    private val repository = ApiKeyRepositoryImpl(mockApiKeyStore)

    @Test
    fun `getApiKey returns the stored key`() {
        every { mockApiKeyStore.getKey(LlmProviderType.OPENAI) } returns INPUT_API_KEY
        val actual = repository.getApiKey(LlmProviderType.OPENAI)
        assertEquals(INPUT_API_KEY, actual)
    }

    @Test
    fun `getApiKey returns null when no key was stored`() {
        every { mockApiKeyStore.getKey(LlmProviderType.CLAUDE) } returns null
        val actual = repository.getApiKey(LlmProviderType.CLAUDE)
        assertNull(actual)
    }

    @Test
    fun `saveApiKey delegates to the encrypted store`() {
        repository.saveApiKey(LlmProviderType.GEMINI_API, INPUT_API_KEY)
        verify { mockApiKeyStore.saveKey(LlmProviderType.GEMINI_API, INPUT_API_KEY) }
    }

    @Test
    fun `clearApiKey delegates to the encrypted store`() {
        repository.clearApiKey(LlmProviderType.GEMINI_API)
        verify { mockApiKeyStore.clearKey(LlmProviderType.GEMINI_API) }
    }

    private companion object {
        const val INPUT_API_KEY = "test-api-key"
    }
}
