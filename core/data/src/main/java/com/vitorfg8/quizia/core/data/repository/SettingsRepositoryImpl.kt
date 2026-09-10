package com.vitorfg8.quizia.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.vitorfg8.quizia.core.data.local.PreferencesKeys
import com.vitorfg8.quizia.core.data.local.toAppSettings
import com.vitorfg8.quizia.core.domain.model.AppSettings
import com.vitorfg8.quizia.core.domain.model.AppTheme
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {

    override fun observeSettings(): Flow<AppSettings> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences -> preferences.toAppSettings() }

    override suspend fun saveSelectedProvider(provider: LlmProviderType) {
        dataStore.edit { it[PreferencesKeys.SELECTED_PROVIDER] = provider.name }
    }

    override suspend fun saveTheme(theme: AppTheme) {
        dataStore.edit { it[PreferencesKeys.APP_THEME] = theme.name }
    }

    override suspend fun saveQuestionCount(count: Int) {
        dataStore.edit { it[PreferencesKeys.QUESTION_COUNT] = count }
    }

    override suspend fun setFirstRunComplete() {
        dataStore.edit { it[PreferencesKeys.IS_FIRST_RUN] = false }
    }
}
