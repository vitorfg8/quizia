package com.vitorfg8.quizia.feature.quiz.di

import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.core.domain.usecase.GenerateQuizUseCase
import com.vitorfg8.quizia.feature.quiz.QuizViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val quizModule = module {
    single { GenerateQuizUseCase(settingsRepository = get(), llmProvider = get()) }
    viewModel { (category: QuizCategory) ->
        QuizViewModel(category = category, generateQuiz = get())
    }
}
