package com.vitorfg8.quizia.core.llm.provider

import com.google.gson.Gson
import com.vitorfg8.quizia.core.domain.exception.LlmException
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.core.domain.model.QuizRequest
import com.vitorfg8.quizia.core.domain.repository.ApiKeyRepository
import com.vitorfg8.quizia.core.llm.parser.QuizJsonParser
import com.vitorfg8.quizia.core.llm.prompt.QuizPromptBuilder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class RemoteQuizGeneratorTest {

    private val mockApiKeyRepository = mockk<ApiKeyRepository>()
    private val generator = RemoteQuizGenerator(
        apiKeyRepository = mockApiKeyRepository,
        promptBuilder = QuizPromptBuilder(),
        parser = QuizJsonParser(Gson()),
    )

    @Test
    fun `a valid completion becomes a quiz`() = runTest {
        givenStoredKey(INPUT_API_KEY)
        val actual = generator.generate(PROVIDER, buildRequest()) { _, _ -> VALID_PAYLOAD }
        assertEquals(1, actual.getOrThrow().questions.size)
    }

    @Test
    fun `the stored key and the built prompt reach the http call`() = runTest {
        givenStoredKey(INPUT_API_KEY)
        var actualKey: String? = null
        var actualPrompt: String? = null
        generator.generate(PROVIDER, buildRequest()) { apiKey, prompt ->
            actualKey = apiKey
            actualPrompt = prompt
            VALID_PAYLOAD
        }
        assertEquals(INPUT_API_KEY, actualKey)
        assertTrue(actualPrompt.orEmpty().contains("astronomy"))
    }

    @Test
    fun `a missing key fails before any http call`() = runTest {
        givenStoredKey(null)
        var wasCalled = false
        val actual = generator.generate(PROVIDER, buildRequest()) { _, _ ->
            wasCalled = true
            VALID_PAYLOAD
        }
        assertTrue(actual.exceptionOrNull() is LlmException.MissingApiKey)
        assertTrue(!wasCalled)
    }

    @Test
    fun `a blank key is treated as missing`() = runTest {
        givenStoredKey("   ")
        val actual = generator.generate(PROVIDER, buildRequest()) { _, _ -> VALID_PAYLOAD }
        assertTrue(actual.exceptionOrNull() is LlmException.MissingApiKey)
    }

    @Test
    fun `a network failure is propagated`() = runTest {
        givenStoredKey(INPUT_API_KEY)
        val actual = generator.generate(PROVIDER, buildRequest()) { _, _ ->
            throw IOException("host unreachable")
        }
        assertTrue(actual.exceptionOrNull() is IOException)
    }

    @Test
    fun `a null completion reports an empty response`() = runTest {
        givenStoredKey(INPUT_API_KEY)
        val actual = generator.generate(PROVIDER, buildRequest()) { _, _ -> null }
        assertTrue(actual.exceptionOrNull() is LlmException.EmptyResponse)
    }

    @Test
    fun `a blank completion reports an empty response`() = runTest {
        givenStoredKey(INPUT_API_KEY)
        val actual = generator.generate(PROVIDER, buildRequest()) { _, _ -> "  " }
        assertTrue(actual.exceptionOrNull() is LlmException.EmptyResponse)
    }

    @Test
    fun `an unparseable completion reports an invalid response`() = runTest {
        givenStoredKey(INPUT_API_KEY)
        val actual = generator.generate(PROVIDER, buildRequest()) { _, _ -> "not a quiz" }
        assertTrue(actual.exceptionOrNull() is LlmException.InvalidResponse)
    }

    private fun givenStoredKey(key: String?) {
        every { mockApiKeyRepository.getApiKey(PROVIDER) } returns key
    }

    private fun buildRequest() = QuizRequest(
        category = QuizCategory.ASTRONOMY,
        questionCount = 5,
        language = "en",
    )

    private companion object {
        val PROVIDER = LlmProviderType.OPENAI
        const val INPUT_API_KEY = "test-api-key"
        val VALID_PAYLOAD = """
            {"questions":[{"text":"Largest planet?","options":["Mars","Jupiter","Venus","Mercury"],"correctIndex":1}]}
        """.trimIndent()
    }
}
