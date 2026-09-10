package com.vitorfg8.quizia.core.llm.provider

import com.google.gson.Gson
import com.vitorfg8.quizia.core.domain.exception.LlmException
import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.core.domain.model.QuizRequest
import com.vitorfg8.quizia.core.llm.ondevice.OnDeviceModelSession
import com.vitorfg8.quizia.core.llm.parser.QuizJsonParser
import com.vitorfg8.quizia.core.llm.prompt.QuizPromptBuilder
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GeminiNanoProviderTest {

    private val mockSession = mockk<OnDeviceModelSession>()
    private val provider = GeminiNanoProvider(
        session = mockSession,
        promptBuilder = QuizPromptBuilder(),
        parser = QuizJsonParser(Gson()),
    )

    @Test
    fun `a valid on-device answer becomes a quiz`() = runTest {
        coEvery { mockSession.prepare() } returns true
        coEvery { mockSession.generate(any()) } returns VALID_PAYLOAD
        val actual = provider.generateQuiz(buildRequest())
        assertEquals(1, actual.getOrThrow().questions.size)
    }

    @Test
    fun `an unprepared model fails without generating`() = runTest {
        coEvery { mockSession.prepare() } returns false
        val actual = provider.generateQuiz(buildRequest())
        assertTrue(actual.exceptionOrNull() is LlmException.OnDeviceModelUnavailable)
        coVerify(exactly = 0) { mockSession.generate(any()) }
    }

    @Test
    fun `a runtime failure while preparing is reported as unavailable`() = runTest {
        coEvery { mockSession.prepare() } throws IllegalStateException("aicore missing")
        val actual = provider.generateQuiz(buildRequest())
        assertTrue(actual.exceptionOrNull() is LlmException.OnDeviceModelUnavailable)
    }

    @Test
    fun `a failure while generating is propagated`() = runTest {
        coEvery { mockSession.prepare() } returns true
        coEvery { mockSession.generate(any()) } throws IllegalStateException("inference failed")
        val actual = provider.generateQuiz(buildRequest())
        assertTrue(actual.exceptionOrNull() is IllegalStateException)
    }

    @Test
    fun `a blank answer reports an empty response`() = runTest {
        coEvery { mockSession.prepare() } returns true
        coEvery { mockSession.generate(any()) } returns ""
        val actual = provider.generateQuiz(buildRequest())
        assertTrue(actual.exceptionOrNull() is LlmException.EmptyResponse)
    }

    @Test
    fun `a null answer reports an empty response`() = runTest {
        coEvery { mockSession.prepare() } returns true
        coEvery { mockSession.generate(any()) } returns null
        val actual = provider.generateQuiz(buildRequest())
        assertTrue(actual.exceptionOrNull() is LlmException.EmptyResponse)
    }

    @Test
    fun `an unparseable answer reports an invalid response`() = runTest {
        coEvery { mockSession.prepare() } returns true
        coEvery { mockSession.generate(any()) } returns "sorry, I cannot do that"
        val actual = provider.generateQuiz(buildRequest())
        assertTrue(actual.exceptionOrNull() is LlmException.InvalidResponse)
    }

    private fun buildRequest() = QuizRequest(
        category = QuizCategory.GENERAL_KNOWLEDGE,
        questionCount = 5,
        language = "en",
    )

    private companion object {
        val VALID_PAYLOAD = """
            {"questions":[{"text":"Largest planet?","options":["Mars","Jupiter","Venus","Mercury"],"correctIndex":1}]}
        """.trimIndent()
    }
}
