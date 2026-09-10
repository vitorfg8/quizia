package com.vitorfg8.quizia.core.domain.usecase

import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.core.domain.model.QuizRequest
import com.vitorfg8.quizia.core.domain.model.QuizResponse
import com.vitorfg8.quizia.core.domain.repository.LlmProvider
import com.vitorfg8.quizia.core.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import java.util.Locale

/**
 * Builds a quiz for [QuizCategory] using the question count the user configured and the
 * language the app is currently running in.
 */
class GenerateQuizUseCase(
    private val settingsRepository: SettingsRepository,
    private val llmProvider: LlmProvider,
) {

    suspend operator fun invoke(
        category: QuizCategory,
        language: String = Locale.getDefault().language,
    ): Result<QuizResponse> {
        val settings = settingsRepository.observeSettings().first()
        val request = QuizRequest(
            category = category,
            questionCount = settings.questionCount,
            language = language,
        )
        return llmProvider.generateQuiz(request)
    }
}
