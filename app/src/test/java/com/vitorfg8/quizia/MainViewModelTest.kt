package com.vitorfg8.quizia

import com.vitorfg8.quizia.core.domain.model.AppSettings
import com.vitorfg8.quizia.core.domain.model.AppTheme
import com.vitorfg8.quizia.core.domain.repository.SettingsRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val mockSettingsRepository = mockk<SettingsRepository>()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `isFirstRun starts as null while settings are loading`() {
        every { mockSettingsRepository.observeSettings() } returns flowOf()
        val viewModel = MainViewModel(mockSettingsRepository)
        assertNull(viewModel.isFirstRun.value)
    }

    @Test
    fun `isFirstRun is true when onboarding was never completed`() = runTest {
        every { mockSettingsRepository.observeSettings() } returns flowOf(AppSettings(isFirstRun = true))
        val viewModel = MainViewModel(mockSettingsRepository)
        val actual = viewModel.isFirstRun.filterNotNull().first()
        assertEquals(true, actual)
    }

    @Test
    fun `isFirstRun is false once onboarding was completed`() = runTest {
        every { mockSettingsRepository.observeSettings() } returns flowOf(AppSettings(isFirstRun = false))
        val viewModel = MainViewModel(mockSettingsRepository)
        val actual = viewModel.isFirstRun.filterNotNull().first()
        assertEquals(false, actual)
    }

    @Test
    fun `theme starts as system until settings are loaded`() {
        every { mockSettingsRepository.observeSettings() } returns flowOf()
        val viewModel = MainViewModel(mockSettingsRepository)
        assertEquals(AppTheme.SYSTEM, viewModel.theme.value)
    }

    @Test
    fun `theme follows the stored preference`() = runTest {
        every { mockSettingsRepository.observeSettings() } returns
            flowOf(AppSettings(theme = AppTheme.DARK))
        val viewModel = MainViewModel(mockSettingsRepository)
        val actual = viewModel.theme.first { theme -> theme == AppTheme.DARK }
        assertEquals(AppTheme.DARK, actual)
    }
}
