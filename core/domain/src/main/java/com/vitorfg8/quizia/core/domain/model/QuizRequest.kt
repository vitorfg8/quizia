package com.vitorfg8.quizia.core.domain.model

/** Everything a [com.vitorfg8.quizia.core.domain.repository.LlmProvider] needs to build a quiz. */
data class QuizRequest(
    val category: QuizCategory,
    val questionCount: Int,
    val language: String,
)
