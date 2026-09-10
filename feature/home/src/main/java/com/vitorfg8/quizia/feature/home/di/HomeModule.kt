package com.vitorfg8.quizia.feature.home.di

import com.vitorfg8.quizia.core.domain.usecase.HasAvailableLlmProviderUseCase
import com.vitorfg8.quizia.feature.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel {
        HomeViewModel(
            hasAvailableLlmProvider = HasAvailableLlmProviderUseCase(
                apiKeyRepository = get(),
                onDeviceModelRepository = get(),
            ),
        )
    }
}
