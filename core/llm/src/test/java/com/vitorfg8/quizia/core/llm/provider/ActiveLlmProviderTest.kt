package com.vitorfg8.quizia.core.llm.provider

import com.vitorfg8.quizia.core.domain.exception.LlmException
import com.vitorfg8.quizia.core.domain.model.AppSettings
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.model.Question
import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.core.domain.model.QuizRequest
import com.vitorfg8.quizia.core.domain.model.QuizResponse
import com.vitorfg8.quizia.core.domain.repository.LlmProvider
import com.vitorfg8.quizia.core.domain.repository.SettingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ActiveLlmProviderTest {

    private val mockSettingsRepository = mockk<SettingsRepository>()
    private val mockOpenAiProvider = mockk<LlmProvider>()
    private val mockClaudeProvider = mockk<LlmProvider>()

    @Test
    fun `the request goes to the selected provider`() = runTest {
        givenSelectedProvider(LlmProviderType.CLAUDE)
        val expected = QuizResponse(listOf(buildQuestion()))
        coEvery { mockClaudeProvider.generateQuiz(any()) } returns Result.success(expected)
        val actual = buildActiveProvider().generateQuiz(buildRequest())
        assertEquals(expected, actual.getOrNull())
        coVerify(exactly = 0) { mockOpenAiProvider.generateQuiz(any()) }
    }

    @Test
    fun `the selection is re-read on every request`() = runTest {
        givenSelectedProvider(LlmProviderType.OPENAI)
        coEvery { mockOpenAiProvider.generateQuiz(any()) } returns
            Result.success(QuizResponse(listOf(buildQuestion())))
        val provider = buildActiveProvider()
        provider.generateQuiz(buildRequest())
        provider.generateQuiz(buildRequest())
        coVerify(exactly = 2) { mockSettingsRepository.observeSettings() }
    }

    @Test
    fun `an unregistered provider fails instead of crashing`() = runTest {
        givenSelectedProvider(LlmProviderType.GEMINI_NANO)
        val actual = buildActiveProvider().generateQuiz(buildRequest())
        assertTrue(actual.exceptionOrNull() is LlmException.MissingApiKey)
    }

    private fun givenSelectedProvider(providerType: LlmProviderType) {
        every { mockSettingsRepository.observeSettings() } returns
            flowOf(AppSettings(selectedProvider = providerType))
    }

    private fun buildActiveProvider() = ActiveLlmProvider(
        settingsRepository = mockSettingsRepository,
        providers = mapOf(
            LlmProviderType.OPENAI to mockOpenAiProvider,
            LlmProviderType.CLAUDE to mockClaudeProvider,
        ),
    )

    private fun buildRequest() = QuizRequest(
        category = QuizCategory.SPORTS,
        questionCount = 5,
        language = "en",
    )

    private fun buildQuestion() = Question(
        text = "Largest planet?",
        options = listOf("Mars", "Jupiter", "Venus", "Mercury"),
        correctIndex = 1,
    )
}
