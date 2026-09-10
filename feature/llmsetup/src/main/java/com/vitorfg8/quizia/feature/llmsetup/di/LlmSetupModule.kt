package com.vitorfg8.quizia.feature.llmsetup.di

import com.vitorfg8.quizia.feature.llmsetup.LlmSetupViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val llmSetupModule = module {
    viewModel {
        LlmSetupViewModel(
            settingsRepository = get(),
            apiKeyRepository = get(),
            isGeminiNanoSupported = get(named("isGeminiNanoSupported")),
        )
    }
}
