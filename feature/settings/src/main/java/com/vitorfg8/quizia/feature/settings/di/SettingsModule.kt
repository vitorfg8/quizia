package com.vitorfg8.quizia.feature.settings.di

import com.vitorfg8.quizia.feature.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    viewModel {
        SettingsViewModel(
            settingsRepository = get(),
            apiKeyRepository = get(),
            onDeviceModelRepository = get(),
        )
    }
}
