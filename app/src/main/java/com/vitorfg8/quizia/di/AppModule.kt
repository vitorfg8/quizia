package com.vitorfg8.quizia.di

import com.vitorfg8.quizia.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel(settingsRepository = get()) }
}
