package com.vitorfg8.quizia.core.llm.provider

import com.google.gson.Gson
import com.vitorfg8.quizia.core.domain.exception.LlmException
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.core.domain.model.QuizRequest
import com.vitorfg8.quizia.core.domain.repository.ApiKeyRepository
import com.vitorfg8.quizia.core.llm.network.OpenAiService
import com.vitorfg8.quizia.core.llm.network.dto.OpenAiChoiceDto
import com.vitorfg8.quizia.core.llm.network.dto.OpenAiMessageDto
import com.vitorfg8.quizia.core.llm.network.dto.OpenAiRequestDto
import com.vitorfg8.quizia.core.llm.network.dto.OpenAiResponseDto
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

class OpenAiProviderTest {

    private val mockService = mockk<OpenAiService>()
    private val mockApiKeyRepository = mockk<ApiKeyRepository>()
    private val provider = OpenAiProvider(
        service = mockService,
        generator = RemoteQuizGenerator(
            apiKeyRepository = mockApiKeyRepository,
            promptBuilder = QuizPromptBuilder(),
            parser = QuizJsonParser(Gson()),
        ),
    )

    @Test
    fun `the first choice becomes a quiz`() = runTest {
        givenStoredKey()
        coEvery { mockService.createChatCompletion(any(), any()) } returns responseWith(VALID_PAYLOAD)
        val actual = provider.generateQuiz(buildRequest())
        assertEquals(1, actual.getOrThrow().questions.size)
    }

    @Test
    fun `the key is sent as a bearer token`() = runTest {
        givenStoredKey()
        val authorizationSlot = slot<String>()
        coEvery {
            mockService.createChatCompletion(capture(authorizationSlot), any())
        } returns responseWith(VALID_PAYLOAD)
        provider.generateQuiz(buildRequest())
        assertEquals("Bearer $INPUT_API_KEY", authorizationSlot.captured)
    }

    @Test
    fun `the request asks for a json object and carries the prompt`() = runTest {
        givenStoredKey()
        val bodySlot = slot<OpenAiRequestDto>()
        coEvery {
            mockService.createChatCompletion(any(), capture(bodySlot))
        } returns responseWith(VALID_PAYLOAD)
        provider.generateQuiz(buildRequest())
        assertEquals("json_object", bodySlot.captured.responseFormat.type)
        assertTrue(bodySlot.captured.messages.first().content.orEmpty().contains("sports"))
    }

    @Test
    fun `a response without choices reports an empty response`() = runTest {
        givenStoredKey()
        coEvery { mockService.createChatCompletion(any(), any()) } returns OpenAiResponseDto()
        val actual = provider.generateQuiz(buildRequest())
        assertTrue(actual.exceptionOrNull() is LlmException.EmptyResponse)
    }

    private fun givenStoredKey() {
        every { mockApiKeyRepository.getApiKey(LlmProviderType.OPENAI) } returns INPUT_API_KEY
    }

    private fun responseWith(text: String) = OpenAiResponseDto(
        choices = listOf(OpenAiChoiceDto(OpenAiMessageDto(role = "assistant", content = text))),
    )

    private fun buildRequest() = QuizRequest(
        category = QuizCategory.SPORTS,
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
