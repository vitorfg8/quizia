package com.vitorfg8.quizia.core.domain.usecase

import com.vitorfg8.quizia.core.domain.model.AppSettings
import com.vitorfg8.quizia.core.domain.model.Question
import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.core.domain.model.QuizRequest
import com.vitorfg8.quizia.core.domain.model.QuizResponse
import com.vitorfg8.quizia.core.domain.repository.LlmProvider
import com.vitorfg8.quizia.core.domain.repository.SettingsRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GenerateQuizUseCaseTest {

    private val mockSettingsRepository = mockk<SettingsRepository>()
    private val mockLlmProvider = mockk<LlmProvider>()
    private val useCase = GenerateQuizUseCase(mockSettingsRepository, mockLlmProvider)

    @Test
    fun `the request carries the configured question count and the given language`() = runTest {
        every { mockSettingsRepository.observeSettings() } returns
            flowOf(AppSettings(questionCount = INPUT_QUESTION_COUNT))
        val requestSlot = slot<QuizRequest>()
        coEvery { mockLlmProvider.generateQuiz(capture(requestSlot)) } returns
            Result.success(QuizResponse(listOf(buildQuestion())))
        useCase(category = QuizCategory.ASTRONOMY, language = "pt")
        val expected = QuizRequest(
            category = QuizCategory.ASTRONOMY,
            questionCount = INPUT_QUESTION_COUNT,
            language = "pt",
        )
        assertEquals(expected, requestSlot.captured)
    }

    @Test
    fun `the generated questions are returned untouched`() = runTest {
        every { mockSettingsRepository.observeSettings() } returns flowOf(AppSettings())
        val expected = QuizResponse(listOf(buildQuestion()))
        coEvery { mockLlmProvider.generateQuiz(any()) } returns Result.success(expected)
        val actual = useCase(category = QuizCategory.SPORTS, language = "en")
        assertEquals(expected, actual.getOrNull())
    }

    @Test
    fun `a provider failure is propagated to the caller`() = runTest {
        every { mockSettingsRepository.observeSettings() } returns flowOf(AppSettings())
        coEvery { mockLlmProvider.generateQuiz(any()) } returns
            Result.failure(IllegalStateException("no api key"))
        val actual = useCase(category = QuizCategory.SPORTS, language = "en")
        assertTrue(actual.isFailure)
    }

    private fun buildQuestion() = Question(
        text = "Which planet is the largest?",
        options = listOf("Mars", "Jupiter", "Venus", "Mercury"),
        correctIndex = 1,
    )

    private companion object {
        const val INPUT_QUESTION_COUNT = 15
    }
}
