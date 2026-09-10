package com.vitorfg8.quizia.core.llm.provider

import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.model.QuizRequest
import com.vitorfg8.quizia.core.domain.model.QuizResponse
import com.vitorfg8.quizia.core.domain.repository.LlmProvider
import com.vitorfg8.quizia.core.llm.network.GeminiApiService
import com.vitorfg8.quizia.core.llm.network.dto.GeminiContentDto
import com.vitorfg8.quizia.core.llm.network.dto.GeminiGenerationConfigDto
import com.vitorfg8.quizia.core.llm.network.dto.GeminiPartDto
import com.vitorfg8.quizia.core.llm.network.dto.GeminiRequestDto
import com.vitorfg8.quizia.core.llm.network.dto.GeminiResponseDto

/** Calls the Gemini Developer REST API with the key the user supplied. */
internal class GeminiApiProvider(
    private val service: GeminiApiService,
    private val generator: RemoteQuizGenerator,
) : LlmProvider {

    override suspend fun generateQuiz(request: QuizRequest): Result<QuizResponse> =
        generator.generate(LlmProviderType.GEMINI_API, request) { apiKey, prompt ->
            service.generateContent(MODEL, apiKey, prompt.toRequestDto()).firstText()
        }

    private fun String.toRequestDto() = GeminiRequestDto(
        contents = listOf(GeminiContentDto(parts = listOf(GeminiPartDto(text = this)))),
        generationConfig = GeminiGenerationConfigDto(responseMimeType = JSON_MIME_TYPE),
    )

    private fun GeminiResponseDto.firstText(): String? = candidates
        ?.firstNotNullOfOrNull { candidate -> candidate.content?.parts }
        ?.firstNotNullOfOrNull { part -> part.text }

    private companion object {
        const val MODEL = "gemini-3.5-flash"
        const val JSON_MIME_TYPE = "application/json"
    }
}
