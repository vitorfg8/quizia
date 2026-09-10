package com.vitorfg8.quizia.core.llm.provider

import com.google.gson.Gson
import com.vitorfg8.quizia.core.domain.exception.LlmException
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.core.domain.model.QuizRequest
import com.vitorfg8.quizia.core.domain.repository.ApiKeyRepository
import com.vitorfg8.quizia.core.llm.network.GeminiApiService
import com.vitorfg8.quizia.core.llm.network.dto.GeminiCandidateDto
import com.vitorfg8.quizia.core.llm.network.dto.GeminiContentDto
import com.vitorfg8.quizia.core.llm.network.dto.GeminiPartDto
import com.vitorfg8.quizia.core.llm.network.dto.GeminiRequestDto
import com.vitorfg8.quizia.core.llm.network.dto.GeminiResponseDto
import com.vitorfg8.quizia.core.llm.parser.QuizJsonParser
import com.vitorfg8.quizia.core.llm.prompt.QuizPromptBuilder
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GeminiApiProviderTest {

    private val mockService = mockk<GeminiApiService>()
    private val mockApiKeyRepository = mockk<ApiKeyRepository>()
    private val provider = GeminiApiProvider(
        service = mockService,
        generator = RemoteQuizGenerator(
            apiKeyRepository = mockApiKeyRepository,
            promptBuilder = QuizPromptBuilder(),
            parser = QuizJsonParser(Gson()),
        ),
    )

    @Test
    fun `the candidate text becomes a quiz`() = runTest {
        givenStoredKey()
        coEvery { mockService.generateContent(any(), any(), any()) } returns responseWith(VALID_PAYLOAD)
        val actual = provider.generateQuiz(buildRequest())
        assertEquals(1, actual.getOrThrow().questions.size)
    }

    @Test
    fun `the stored key and the prompt are sent to the endpoint`() = runTest {
        givenStoredKey()
        val keySlot = slot<String>()
        val bodySlot = slot<GeminiRequestDto>()
        coEvery {
            mockService.generateContent(any(), capture(keySlot), capture(bodySlot))
        } returns responseWith(VALID_PAYLOAD)
        provider.generateQuiz(buildRequest())
        assertEquals(INPUT_API_KEY, keySlot.captured)
        val sentPrompt = bodySlot.captured.contents.first().parts?.first()?.text.orEmpty()
        assertTrue(sentPrompt.contains("astronomy"))
    }

    @Test
    fun `json output is requested from the model`() = runTest {
        givenStoredKey()
        val bodySlot = slot<GeminiRequestDto>()
        coEvery {
            mockService.generateContent(any(), any(), capture(bodySlot))
        } returns responseWith(VALID_PAYLOAD)
        provider.generateQuiz(buildRequest())
        assertEquals("application/json", bodySlot.captured.generationConfig.responseMimeType)
    }

    @Test
    fun `a response without candidates reports an empty response`() = runTest {
        givenStoredKey()
        coEvery { mockService.generateContent(any(), any(), any()) } returns GeminiResponseDto()
        val actual = provider.generateQuiz(buildRequest())
        assertTrue(actual.exceptionOrNull() is LlmException.EmptyResponse)
    }

    private fun givenStoredKey() {
        every { mockApiKeyRepository.getApiKey(LlmProviderType.GEMINI_API) } returns INPUT_API_KEY
    }

    private fun responseWith(text: String) = GeminiResponseDto(
        candidates = listOf(
            GeminiCandidateDto(GeminiContentDto(parts = listOf(GeminiPartDto(text = text)))),
        ),
    )

    private fun buildRequest() = QuizRequest(
        category = QuizCategory.ASTRONOMY,
        questionCount = 5,
        language = "en",
    )

    private companion object {
        const val INPUT_API_KEY = "test-api-key"
        val VALID_PAYLOAD = """
            {"questions":[{"text":"Largest planet?","options":["Mars","Jupiter","Venus","Mercury"],"correctIndex":1}]}
        """.trimIndent()
    }
}
