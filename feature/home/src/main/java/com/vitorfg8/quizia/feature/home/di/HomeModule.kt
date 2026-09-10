package com.vitorfg8.quizia.feature.home.di

import com.vitorfg8.quizia.feature.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel { HomeViewModel() }
}
