package com.vitorfg8.quizia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorfg8.quizia.core.domain.model.AppTheme
import com.vitorfg8.quizia.core.domain.repository.SettingsRepository
import com.vitorfg8.quizia.core.domain.usecase.HasAvailableLlmProviderUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    settingsRepository: SettingsRepository,
    hasAvailableLlmProvider: HasAvailableLlmProviderUseCase,
) : ViewModel() {

    val startOnWelcome: StateFlow<Boolean?> = settingsRepository
        .observeSettings()
        .mapLatest { settings ->
            settings.isFirstRun || !hasAvailableLlmProvider()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = null,
        )

    val theme: StateFlow<AppTheme> = settingsRepository
        .observeSettings()
        .map { settings -> settings.theme }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = AppTheme.SYSTEM,
        )
}
