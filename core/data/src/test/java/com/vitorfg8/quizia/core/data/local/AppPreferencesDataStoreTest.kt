package com.vitorfg8.quizia.core.data.local

import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesOf
import com.vitorfg8.quizia.core.domain.model.AppSettings
import com.vitorfg8.quizia.core.domain.model.AppTheme
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import org.junit.Assert.assertEquals
import org.junit.Test

class AppPreferencesDataStoreTest {

    @Test
    fun `toAppSettings returns defaults when no preference is stored`() {
        val inputPreferences = emptyPreferences()
        val expected = AppSettings()
        val actual = inputPreferences.toAppSettings()
        assertEquals(expected, actual)
    }

    @Test
    fun `toAppSettings maps every stored preference`() {
        val inputPreferences = preferencesOf(
            PreferencesKeys.SELECTED_PROVIDER to LlmProviderType.CLAUDE.name,
            PreferencesKeys.APP_THEME to AppTheme.DARK.name,
            PreferencesKeys.QUESTION_COUNT to STORED_QUESTION_COUNT,
            PreferencesKeys.IS_FIRST_RUN to false,
        )
        val expected = AppSettings(
            selectedProvider = LlmProviderType.CLAUDE,
            theme = AppTheme.DARK,
            questionCount = STORED_QUESTION_COUNT,
            isFirstRun = false,
        )
        val actual = inputPreferences.toAppSettings()
        assertEquals(expected, actual)
    }

    @Test
    fun `toAppSettings falls back to defaults when stored enum names are unknown`() {
        val inputPreferences = preferencesOf(
            PreferencesKeys.SELECTED_PROVIDER to "REMOVED_PROVIDER",
            PreferencesKeys.APP_THEME to "REMOVED_THEME",
        )
        val expected = AppSettings()
        val actual = inputPreferences.toAppSettings()
        assertEquals(expected, actual)
    }

    private companion object {
        const val STORED_QUESTION_COUNT = 15
    }
}
