package com.vitorfg8.quizia.core.llm.provider

import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.model.QuizRequest
import com.vitorfg8.quizia.core.domain.model.QuizResponse
import com.vitorfg8.quizia.core.domain.repository.LlmProvider
import com.vitorfg8.quizia.core.llm.network.ClaudeService
import com.vitorfg8.quizia.core.llm.network.dto.ClaudeMessageDto
import com.vitorfg8.quizia.core.llm.network.dto.ClaudeRequestDto
import com.vitorfg8.quizia.core.llm.network.dto.ClaudeResponseDto

/** Calls the Claude messages API with the key the user supplied. */
internal class ClaudeProvider(
    private val service: ClaudeService,
    private val generator: RemoteQuizGenerator,
) : LlmProvider {

    override suspend fun generateQuiz(request: QuizRequest): Result<QuizResponse> =
        generator.generate(LlmProviderType.CLAUDE, request) { apiKey, prompt ->
            service.createMessage(apiKey, ANTHROPIC_VERSION, prompt.toRequestDto()).firstText()
        }

    private fun String.toRequestDto() = ClaudeRequestDto(
        model = MODEL,
        maxTokens = MAX_TOKENS,
        messages = listOf(ClaudeMessageDto(role = USER_ROLE, content = this)),
    )

    private fun ClaudeResponseDto.firstText(): String? =
        content?.firstNotNullOfOrNull { block -> block.text }

    private companion object {
        const val MODEL = "claude-haiku-4-5"
        const val ANTHROPIC_VERSION = "2023-06-01"
        const val USER_ROLE = "user"
        const val MAX_TOKENS = 4096
    }
}
