package com.vitorfg8.quizia.feature.settings

sealed interface SettingsSideEffect {
    data object NavigateBack : SettingsSideEffect
}
