package com.vitorfg8.quizia.core.llm.provider

import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.model.QuizRequest
import com.vitorfg8.quizia.core.domain.model.QuizResponse
import com.vitorfg8.quizia.core.domain.repository.LlmProvider
import com.vitorfg8.quizia.core.llm.network.OpenAiService
import com.vitorfg8.quizia.core.llm.network.dto.OpenAiMessageDto
import com.vitorfg8.quizia.core.llm.network.dto.OpenAiRequestDto
import com.vitorfg8.quizia.core.llm.network.dto.OpenAiResponseDto
import com.vitorfg8.quizia.core.llm.network.dto.OpenAiResponseFormatDto

/** Calls the OpenAI chat completions API with the key the user supplied. */
internal class OpenAiProvider(
    private val service: OpenAiService,
    private val generator: RemoteQuizGenerator,
) : LlmProvider {

    override suspend fun generateQuiz(request: QuizRequest): Result<QuizResponse> =
        generator.generate(LlmProviderType.OPENAI, request) { apiKey, prompt ->
            service.createChatCompletion("$BEARER_PREFIX$apiKey", prompt.toRequestDto()).firstText()
        }

    private fun String.toRequestDto() = OpenAiRequestDto(
        model = MODEL,
        messages = listOf(OpenAiMessageDto(role = USER_ROLE, content = this)),
        responseFormat = OpenAiResponseFormatDto(type = JSON_OBJECT_FORMAT),
    )

    private fun OpenAiResponseDto.firstText(): String? =
        choices?.firstNotNullOfOrNull { choice -> choice.message?.content }

    private companion object {
        const val MODEL = "gpt-5.6-luna"
        const val BEARER_PREFIX = "Bearer "
        const val USER_ROLE = "user"
        const val JSON_OBJECT_FORMAT = "json_object"
    }
}
