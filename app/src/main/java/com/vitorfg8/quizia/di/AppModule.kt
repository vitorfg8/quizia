package com.vitorfg8.quizia.di

import com.vitorfg8.quizia.MainViewModel
import com.vitorfg8.quizia.core.domain.usecase.HasAvailableLlmProviderUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        MainViewModel(
            settingsRepository = get(),
            hasAvailableLlmProvider = HasAvailableLlmProviderUseCase(
                apiKeyRepository = get(),
                onDeviceModelRepository = get(),
            ),
        )
    }
}
