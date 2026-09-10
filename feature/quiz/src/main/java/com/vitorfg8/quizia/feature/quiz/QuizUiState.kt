package com.vitorfg8.quizia.feature.quiz

import androidx.annotation.StringRes
import com.vitorfg8.quizia.core.domain.model.Question

data class QuizUiState(
    val isLoading: Boolean = false,
    val currentQuestionIndex: Int = 0,
    val totalQuestions: Int = 0,
    val question: Question? = null,
    val selectedOptionIndex: Int? = null,
    val answerRevealed: Boolean = false,
    val correctAnswerText: String = "",
    @StringRes val errorMessageResId: Int? = null,
) {
    val isLastQuestion: Boolean get() = currentQuestionIndex == totalQuestions - 1

    val progress: Float
        get() = if (totalQuestions <= 0) 0f else (currentQuestionIndex + 1).toFloat() / totalQuestions
}
