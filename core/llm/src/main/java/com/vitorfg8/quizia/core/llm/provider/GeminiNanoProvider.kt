package com.vitorfg8.quizia.core.llm.provider

import com.vitorfg8.quizia.core.domain.exception.LlmException
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.model.QuizRequest
import com.vitorfg8.quizia.core.domain.model.QuizResponse
import com.vitorfg8.quizia.core.domain.repository.LlmProvider
import com.vitorfg8.quizia.core.llm.ondevice.OnDeviceModelSession
import com.vitorfg8.quizia.core.llm.parser.QuizJsonParser
import com.vitorfg8.quizia.core.llm.prompt.QuizPromptBuilder

/** Runs the quiz prompt on-device through Gemini Nano. Needs no API key. */
internal class GeminiNanoProvider(
    private val session: OnDeviceModelSession,
    private val promptBuilder: QuizPromptBuilder,
    private val parser: QuizJsonParser,
) : LlmProvider {

    override suspend fun generateQuiz(request: QuizRequest): Result<QuizResponse> {
        val isReady = runCatching { session.prepare() }.getOrDefault(false)
        if (!isReady) return Result.failure(LlmException.OnDeviceModelUnavailable())
        return runCatching { session.generate(promptBuilder.build(request)) }
            .mapCatching { rawResponse ->
                rawResponse?.takeIf { it.isNotBlank() } ?: throw LlmException.EmptyResponse(PROVIDER)
            }
            .mapCatching { rawResponse ->
                parser.parse(rawResponse).getOrElse { throw LlmException.InvalidResponse(PROVIDER) }
            }
    }

    private companion object {
        val PROVIDER = LlmProviderType.GEMINI_NANO
    }
}
