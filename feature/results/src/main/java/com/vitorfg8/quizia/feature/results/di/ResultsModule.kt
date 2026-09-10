package com.vitorfg8.quizia.feature.results.di

import com.vitorfg8.quizia.core.domain.usecase.CalculateStarRatingUseCase
import com.vitorfg8.quizia.feature.results.ResultsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val resultsModule = module {
    single { CalculateStarRatingUseCase() }
    viewModel { (score: Int, total: Int) ->
        ResultsViewModel(score = score, total = total, calculateStarRating = get())
    }
}
