package com.vitorfg8.quizia.core.domain.repository

import com.vitorfg8.quizia.core.domain.model.QuizRequest
import com.vitorfg8.quizia.core.domain.model.QuizResponse

/** Generates quiz questions. Implemented once per supported LLM in `:core:llm`. */
interface LlmProvider {
    suspend fun generateQuiz(request: QuizRequest): Result<QuizResponse>
}
