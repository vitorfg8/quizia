package com.vitorfg8.quizia.core.llm.provider

import com.vitorfg8.quizia.core.domain.exception.LlmException
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.model.QuizRequest
import com.vitorfg8.quizia.core.domain.model.QuizResponse
import com.vitorfg8.quizia.core.domain.repository.LlmProvider
import com.vitorfg8.quizia.core.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first

/**
 * Delegates to whichever provider the user selected. The choice is read per request so a change
 * in settings takes effect on the next quiz without rebuilding the graph.
 */
internal class ActiveLlmProvider(
    private val settingsRepository: SettingsRepository,
    private val providers: Map<LlmProviderType, LlmProvider>,
) : LlmProvider {

    override suspend fun generateQuiz(request: QuizRequest): Result<QuizResponse> {
        val selected = settingsRepository.observeSettings().first().selectedProvider
        val provider = providers[selected]
            ?: return Result.failure(LlmException.MissingApiKey(selected))
        return provider.generateQuiz(request)
    }
}
