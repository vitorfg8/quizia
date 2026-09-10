package com.vitorfg8.quizia.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesOf
import com.vitorfg8.quizia.core.data.local.PreferencesKeys
import com.vitorfg8.quizia.core.domain.model.AppSettings
import com.vitorfg8.quizia.core.domain.model.AppTheme
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class SettingsRepositoryImplTest {

    private val mockDataStore = mockk<DataStore<Preferences>>()
    private val repository = SettingsRepositoryImpl(mockDataStore)

    @Test
    fun `observeSettings maps the stored preferences`() = runTest {
        every { mockDataStore.data } returns flowOf(
            preferencesOf(PreferencesKeys.SELECTED_PROVIDER to LlmProviderType.OPENAI.name),
        )
        val expected = AppSettings(selectedProvider = LlmProviderType.OPENAI)
        val actual = repository.observeSettings().first()
        assertEquals(expected, actual)
    }

    @Test
    fun `observeSettings falls back to defaults when reading fails`() = runTest {
        every { mockDataStore.data } returns flow { throw IOException("disk unavailable") }
        val expected = AppSettings()
        val actual = repository.observeSettings().first()
        assertEquals(expected, actual)
    }

    @Test(expected = IllegalStateException::class)
    fun `observeSettings rethrows non IO failures`() = runTest {
        every { mockDataStore.data } returns flow { throw IllegalStateException("corrupted") }
        repository.observeSettings().first()
    }

    @Test
    fun `saveSelectedProvider writes the provider name`() = runTest {
        val actual = captureEditedPreferences { repository.saveSelectedProvider(LlmProviderType.CLAUDE) }
        assertEquals(LlmProviderType.CLAUDE.name, actual[PreferencesKeys.SELECTED_PROVIDER])
    }

    @Test
    fun `saveTheme writes the theme name`() = runTest {
        val actual = captureEditedPreferences { repository.saveTheme(AppTheme.LIGHT) }
        assertEquals(AppTheme.LIGHT.name, actual[PreferencesKeys.APP_THEME])
    }

    @Test
    fun `saveQuestionCount writes the count`() = runTest {
        val actual = captureEditedPreferences { repository.saveQuestionCount(INPUT_QUESTION_COUNT) }
        assertEquals(INPUT_QUESTION_COUNT, actual[PreferencesKeys.QUESTION_COUNT])
    }

    @Test
    fun `setFirstRunComplete clears the first run flag`() = runTest {
        val actual = captureEditedPreferences { repository.setFirstRunComplete() }
        assertEquals(false, actual[PreferencesKeys.IS_FIRST_RUN])
    }

    private suspend fun captureEditedPreferences(edit: suspend () -> Unit): Preferences {
        val transformSlot = slot<suspend (Preferences) -> Preferences>()
        coEvery { mockDataStore.updateData(capture(transformSlot)) } returns emptyPreferences()
        edit()
        return transformSlot.captured(emptyPreferences())
    }

    private companion object {
        const val INPUT_QUESTION_COUNT = 10
    }
}
