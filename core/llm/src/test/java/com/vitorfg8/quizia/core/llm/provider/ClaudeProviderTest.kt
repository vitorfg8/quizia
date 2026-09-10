package com.vitorfg8.quizia.core.llm.provider

import com.google.gson.Gson
import com.vitorfg8.quizia.core.domain.exception.LlmException
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.core.domain.model.QuizRequest
import com.vitorfg8.quizia.core.domain.repository.ApiKeyRepository
import com.vitorfg8.quizia.core.llm.network.ClaudeService
import com.vitorfg8.quizia.core.llm.network.dto.ClaudeContentDto
import com.vitorfg8.quizia.core.llm.network.dto.ClaudeRequestDto
import com.vitorfg8.quizia.core.llm.network.dto.ClaudeResponseDto
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

class ClaudeProviderTest {

    private val mockService = mockk<ClaudeService>()
    private val mockApiKeyRepository = mockk<ApiKeyRepository>()
    private val provider = ClaudeProvider(
        service = mockService,
        generator = RemoteQuizGenerator(
            apiKeyRepository = mockApiKeyRepository,
            promptBuilder = QuizPromptBuilder(),
            parser = QuizJsonParser(Gson()),
        ),
    )

    @Test
    fun `the first text block becomes a quiz`() = runTest {
        givenStoredKey()
        coEvery { mockService.createMessage(any(), any(), any()) } returns responseWith(VALID_PAYLOAD)
        val actual = provider.generateQuiz(buildRequest())
        assertEquals(1, actual.getOrThrow().questions.size)
    }

    @Test
    fun `the key and the api version are sent as headers`() = runTest {
        givenStoredKey()
        val keySlot = slot<String>()
        val versionSlot = slot<String>()
        coEvery {
            mockService.createMessage(capture(keySlot), capture(versionSlot), any())
        } returns responseWith(VALID_PAYLOAD)
        provider.generateQuiz(buildRequest())
        assertEquals(INPUT_API_KEY, keySlot.captured)
        assertEquals("2023-06-01", versionSlot.captured)
    }

    @Test
    fun `the prompt is sent as a user message`() = runTest {
        givenStoredKey()
        val bodySlot = slot<ClaudeRequestDto>()
        coEvery {
            mockService.createMessage(any(), any(), capture(bodySlot))
        } returns responseWith(VALID_PAYLOAD)
        provider.generateQuiz(buildRequest())
        val message = bodySlot.captured.messages.first()
        assertEquals("user", message.role)
        assertTrue(message.content.contains("history and geography"))
    }

    @Test
    fun `a response without content reports an empty response`() = runTest {
        givenStoredKey()
        coEvery { mockService.createMessage(any(), any(), any()) } returns ClaudeResponseDto()
        val actual = provider.generateQuiz(buildRequest())
        assertTrue(actual.exceptionOrNull() is LlmException.EmptyResponse)
    }

    private fun givenStoredKey() {
        every { mockApiKeyRepository.getApiKey(LlmProviderType.CLAUDE) } returns INPUT_API_KEY
    }

    private fun responseWith(text: String) = ClaudeResponseDto(
        content = listOf(ClaudeContentDto(type = "text", text = text)),
    )

    private fun buildRequest() = QuizRequest(
        category = QuizCategory.HISTORY_AND_GEOGRAPHY,
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
