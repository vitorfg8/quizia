package com.vitorfg8.quizia.feature.quiz

sealed interface QuizSideEffect {
    data class NavigateToResults(
        val score: Int,
        val total: Int,
        val elapsedMs: Long,
    ) : QuizSideEffect
}
