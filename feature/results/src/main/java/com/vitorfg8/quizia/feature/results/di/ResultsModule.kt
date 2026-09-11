package com.vitorfg8.quizia.feature.results.di

import com.vitorfg8.quizia.feature.results.ResultsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val resultsModule = module {
    viewModel { (score: Int, total: Int, elapsedMs: Long) ->
        ResultsViewModel(score = score, total = total, elapsedMs = elapsedMs)
    }
}
