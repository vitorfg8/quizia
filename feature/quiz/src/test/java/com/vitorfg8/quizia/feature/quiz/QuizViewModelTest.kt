package com.vitorfg8.quizia.feature.quiz

import com.vitorfg8.quizia.core.domain.exception.LlmException
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.model.Question
import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.core.domain.model.QuizResponse
import com.vitorfg8.quizia.core.domain.usecase.GenerateQuizUseCase
import io.mockk.coEvery
import io.mockk.mockk
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTest {

    private val mockGenerateQuiz = mockk<GenerateQuizUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `the first question is shown once generation succeeds`() {
        givenGeneratedQuiz(buildQuestions())
        val actual = createViewModel().uiState.value
        assertFalse(actual.isLoading)
        assertEquals(0, actual.currentQuestionIndex)
        assertEquals(QUESTION_COUNT, actual.totalQuestions)
        assertEquals(buildQuestions().first(), actual.question)
    }

    @Test
    fun `selecting an option reveals the answer`() {
        givenGeneratedQuiz(buildQuestions())
        val viewModel = createViewModel()
        viewModel.selectOption(optionIndex = 2)
        val actual = viewModel.uiState.value
        assertTrue(actual.answerRevealed)
        assertEquals(2, actual.selectedOptionIndex)
        assertEquals("Jupiter", actual.correctAnswerText)
    }

    @Test
    fun `selecting a second option after the reveal is ignored`() {
        givenGeneratedQuiz(buildQuestions())
        val viewModel = createViewModel()
        viewModel.selectOption(optionIndex = 2)
        viewModel.selectOption(optionIndex = 1)
        assertEquals(2, viewModel.uiState.value.selectedOptionIndex)
    }

    @Test
    fun `answering moves to the next question with a clean slate`() {
        givenGeneratedQuiz(buildQuestions())
        val viewModel = createViewModel()
        viewModel.selectOption(optionIndex = 1)
        viewModel.goToNextQuestion()
        val actual = viewModel.uiState.value
        assertEquals(1, actual.currentQuestionIndex)
        assertEquals(buildQuestions()[1], actual.question)
        assertNull(actual.selectedOptionIndex)
        assertFalse(actual.answerRevealed)
    }

    @Test
    fun `advancing before answering is ignored`() {
        givenGeneratedQuiz(buildQuestions())
        val viewModel = createViewModel()
        viewModel.goToNextQuestion()
        assertEquals(0, viewModel.uiState.value.currentQuestionIndex)
    }

    @Test
    fun `finishing the quiz reports how many answers were right`() = runTest {
        givenGeneratedQuiz(buildQuestions())
        val viewModel = createViewModel()
        answerEveryQuestion(viewModel, correctAnswers = 2)
        val actual = viewModel.sideEffect.first()
        assertEquals(
            QuizSideEffect.NavigateToResults(score = 2, total = QUESTION_COUNT, elapsedMs = 0L),
            actual,
        )
    }

    @Test
    fun `a quiz answered entirely wrong scores zero`() = runTest {
        givenGeneratedQuiz(buildQuestions())
        val viewModel = createViewModel()
        answerEveryQuestion(viewModel, correctAnswers = 0)
        val actual = viewModel.sideEffect.first()
        assertEquals(
            QuizSideEffect.NavigateToResults(score = 0, total = QUESTION_COUNT, elapsedMs = 0L),
            actual,
        )
    }

    @Test
    fun `a missing api key shows the dedicated message`() {
        givenGenerationFailure(LlmException.MissingApiKey(LlmProviderType.OPENAI))
        val actual = createViewModel().uiState.value
        assertEquals(R.string.quiz_error_missing_api_key, actual.errorMessageResId)
        assertFalse(actual.isLoading)
    }

    @Test
    fun `an unavailable on-device model shows the dedicated message`() {
        givenGenerationFailure(LlmException.OnDeviceModelUnavailable())
        val actual = createViewModel().uiState.value
        assertEquals(R.string.quiz_error_on_device_unavailable, actual.errorMessageResId)
    }

    @Test
    fun `any other failure shows the generic message`() {
        givenGenerationFailure(IOException("host unreachable"))
        val actual = createViewModel().uiState.value
        assertEquals(R.string.quiz_error_generic, actual.errorMessageResId)
    }

    @Test
    fun `an empty quiz shows the generic message`() {
        givenGeneratedQuiz(emptyList())
        val actual = createViewModel().uiState.value
        assertEquals(R.string.quiz_error_generic, actual.errorMessageResId)
    }

    @Test
    fun `retrying after a failure loads the quiz again`() {
        givenGenerationFailure(IOException("host unreachable"))
        val viewModel = createViewModel()
        givenGeneratedQuiz(buildQuestions())
        viewModel.loadQuiz()
        val actual = viewModel.uiState.value
        assertNull(actual.errorMessageResId)
        assertEquals(buildQuestions().first(), actual.question)
    }

    @Test
    fun `retrying resets the score`() = runTest {
        givenGeneratedQuiz(buildQuestions())
        val viewModel = createViewModel()
        answerEveryQuestion(viewModel, correctAnswers = QUESTION_COUNT)
        viewModel.loadQuiz()
        answerEveryQuestion(viewModel, correctAnswers = 1)
        viewModel.sideEffect.first()
        val actual = viewModel.sideEffect.first()
        assertEquals(
            QuizSideEffect.NavigateToResults(score = 1, total = QUESTION_COUNT, elapsedMs = 0L),
            actual,
        )
    }

    @Test
    fun `finishing the quiz reports how long it took`() = runTest {
        var nowMs = START_TIME_MS
        givenGeneratedQuiz(buildQuestions())
        val viewModel = createViewModel(now = { nowMs })
        nowMs = START_TIME_MS + ELAPSED_MS
        answerEveryQuestion(viewModel, correctAnswers = 2)
        val actual = viewModel.sideEffect.first()
        val expected = QuizSideEffect.NavigateToResults(
            score = 2,
            total = QUESTION_COUNT,
            elapsedMs = ELAPSED_MS,
        )
        assertEquals(expected, actual)
    }

    private fun answerEveryQuestion(viewModel: QuizViewModel, correctAnswers: Int) {
        buildQuestions().forEachIndexed { index, question ->
            val optionIndex = if (index < correctAnswers) {
                question.correctIndex
            } else {
                (question.correctIndex + 1) % question.options.size
            }
            viewModel.selectOption(optionIndex)
            viewModel.goToNextQuestion()
        }
    }

    private fun givenGeneratedQuiz(questions: List<Question>) {
        coEvery { mockGenerateQuiz(INPUT_CATEGORY, any()) } returns
            Result.success(QuizResponse(questions))
    }

    private fun givenGenerationFailure(failure: Throwable) {
        coEvery { mockGenerateQuiz(INPUT_CATEGORY, any()) } returns Result.failure(failure)
    }

    private fun createViewModel(now: () -> Long = { 0L }) = QuizViewModel(
        category = INPUT_CATEGORY,
        generateQuiz = mockGenerateQuiz,
        now = now,
    )

    private fun buildQuestions(): List<Question> = listOf(
        Question("Largest planet?", listOf("Mars", "Jupiter", "Venus", "Mercury"), correctIndex = 1),
        Question("Closest star?", listOf("Sun", "Sirius", "Vega", "Rigel"), correctIndex = 0),
        Question("Red planet?", listOf("Venus", "Mercury", "Mars", "Saturn"), correctIndex = 2),
    )

    private companion object {
        val INPUT_CATEGORY = QuizCategory.ASTRONOMY
        const val QUESTION_COUNT = 3
        const val START_TIME_MS = 1_000L
        const val ELAPSED_MS = 392_000L
    }
}
