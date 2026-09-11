package com.vitorfg8.quizia.feature.quiz

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorfg8.quizia.core.domain.exception.LlmException
import com.vitorfg8.quizia.core.domain.model.Question
import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.core.domain.usecase.GenerateQuizUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuizViewModel(
    private val category: QuizCategory,
    private val generateQuiz: GenerateQuizUseCase,
    private val now: () -> Long = { SystemClock.elapsedRealtime() },
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState(isLoading = true))
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<QuizSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<QuizSideEffect> = _sideEffect.receiveAsFlow()

    private var questions: List<Question> = emptyList()
    private var score: Int = 0
    private var startedAtMs: Long = 0L

    init {
        loadQuiz()
    }

    fun loadQuiz() {
        _uiState.value = QuizUiState(isLoading = true)
        score = 0
        startedAtMs = 0L
        viewModelScope.launch {
            generateQuiz(category)
                .onSuccess { response -> showQuiz(response.questions) }
                .onFailure { failure -> showError(failure) }
        }
    }

    fun selectOption(optionIndex: Int) {
        val state = _uiState.value
        val question = state.question
        if (question == null || state.answerRevealed) return
        if (optionIndex == question.correctIndex) score++
        _uiState.update {
            it.copy(
                selectedOptionIndex = optionIndex,
                answerRevealed = true,
                correctAnswerText = question.correctOption,
            )
        }
    }

    fun goToNextQuestion() {
        val state = _uiState.value
        if (!state.answerRevealed) return
        if (state.isLastQuestion) {
            finishQuiz()
        } else {
            showQuestionAt(state.currentQuestionIndex + 1)
        }
    }

    private fun showQuiz(loadedQuestions: List<Question>) {
        if (loadedQuestions.isEmpty()) {
            _uiState.value = QuizUiState(errorMessageResId = R.string.quiz_error_generic)
            return
        }
        questions = loadedQuestions
        startedAtMs = now()
        _uiState.value = QuizUiState(totalQuestions = loadedQuestions.size)
        showQuestionAt(0)
    }

    private fun showQuestionAt(index: Int) {
        _uiState.update {
            it.copy(
                isLoading = false,
                currentQuestionIndex = index,
                question = questions[index],
                selectedOptionIndex = null,
                answerRevealed = false,
                correctAnswerText = "",
                errorMessageResId = null,
            )
        }
    }

    private fun finishQuiz() {
        val elapsedMs = (now() - startedAtMs).coerceAtLeast(0L)
        viewModelScope.launch {
            _sideEffect.send(
                QuizSideEffect.NavigateToResults(
                    score = score,
                    total = questions.size,
                    elapsedMs = elapsedMs,
                ),
            )
        }
    }

    private fun showError(failure: Throwable) {
        val messageResId = when (failure) {
            is LlmException.MissingApiKey -> R.string.quiz_error_missing_api_key
            is LlmException.OnDeviceModelUnavailable -> R.string.quiz_error_on_device_unavailable
            else -> R.string.quiz_error_generic
        }
        _uiState.value = QuizUiState(errorMessageResId = messageResId)
    }
}
