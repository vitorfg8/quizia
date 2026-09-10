package com.vitorfg8.quizia.core.data.local

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.vitorfg8.quizia.core.domain.model.AppSettings
import com.vitorfg8.quizia.core.domain.model.AppTheme
import com.vitorfg8.quizia.core.domain.model.LlmProviderType

internal object PreferencesKeys {
    val SELECTED_PROVIDER = stringPreferencesKey("selected_provider")
    val APP_THEME = stringPreferencesKey("app_theme")
    val QUESTION_COUNT = intPreferencesKey("question_count")
    val IS_FIRST_RUN = booleanPreferencesKey("is_first_run")
}

internal fun Preferences.toAppSettings(): AppSettings {
    val defaults = AppSettings()
    return AppSettings(
        selectedProvider = this[PreferencesKeys.SELECTED_PROVIDER]
            ?.let { runCatching { LlmProviderType.valueOf(it) }.getOrNull() }
            ?: defaults.selectedProvider,
        theme = this[PreferencesKeys.APP_THEME]
            ?.let { runCatching { AppTheme.valueOf(it) }.getOrNull() }
            ?: defaults.theme,
        questionCount = this[PreferencesKeys.QUESTION_COUNT] ?: defaults.questionCount,
        isFirstRun = this[PreferencesKeys.IS_FIRST_RUN] ?: defaults.isFirstRun,
    )
}
