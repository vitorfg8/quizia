package com.vitorfg8.quizia.core.llm.provider

import com.vitorfg8.quizia.core.domain.exception.LlmException
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.model.QuizRequest
import com.vitorfg8.quizia.core.domain.model.QuizResponse
import com.vitorfg8.quizia.core.domain.repository.ApiKeyRepository
import com.vitorfg8.quizia.core.llm.parser.QuizJsonParser
import com.vitorfg8.quizia.core.llm.prompt.QuizPromptBuilder

/**
 * The part every cloud provider shares: read the stored key, build the prompt, hand the raw
 * answer to the parser. Only the HTTP call itself differs, so each provider passes it in.
 */
internal class RemoteQuizGenerator(
    private val apiKeyRepository: ApiKeyRepository,
    private val promptBuilder: QuizPromptBuilder,
    private val parser: QuizJsonParser,
) {

    suspend fun generate(
        providerType: LlmProviderType,
        request: QuizRequest,
        requestCompletion: suspend (apiKey: String, prompt: String) -> String?,
    ): Result<QuizResponse> {
        val apiKey = apiKeyRepository.getApiKey(providerType)
        if (apiKey.isNullOrBlank()) return Result.failure(LlmException.MissingApiKey(providerType))
        return runCatching { requestCompletion(apiKey, promptBuilder.build(request)) }
            .mapCatching { rawResponse ->
                rawResponse?.takeIf { it.isNotBlank() }
                    ?: throw LlmException.EmptyResponse(providerType)
            }
            .mapCatching { rawResponse ->
                parser.parse(rawResponse).getOrElse { throw LlmException.InvalidResponse(providerType) }
            }
    }
}
