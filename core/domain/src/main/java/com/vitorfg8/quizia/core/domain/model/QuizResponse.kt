package com.vitorfg8.quizia.core.domain.model

/** The questions a provider generated for a [QuizRequest]. */
data class QuizResponse(
    val questions: List<Question>,
)
