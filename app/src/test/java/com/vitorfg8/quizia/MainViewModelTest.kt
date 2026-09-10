package com.vitorfg8.quizia

import com.vitorfg8.quizia.core.domain.model.AppSettings
import com.vitorfg8.quizia.core.domain.model.AppTheme
import com.vitorfg8.quizia.core.domain.repository.SettingsRepository
import com.vitorfg8.quizia.core.domain.usecase.HasAvailableLlmProviderUseCase
import io.mockk.coEvery
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val mockSettingsRepository = mockk<SettingsRepository>()
    private val mockHasAvailableLlmProvider = mockk<HasAvailableLlmProviderUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coEvery { mockHasAvailableLlmProvider() } returns true
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `startOnWelcome starts as null while settings are loading`() {
        every { mockSettingsRepository.observeSettings() } returns flowOf()
        val viewModel = createViewModel()
        assertNull(viewModel.startOnWelcome.value)
    }

    @Test
    fun `first run always opens welcome`() = runTest {
        every { mockSettingsRepository.observeSettings() } returns
            flowOf(AppSettings(isFirstRun = true))
        val viewModel = createViewModel()
        val actual = viewModel.startOnWelcome.filterNotNull().first()
        assertTrue(actual)
    }

    @Test
    fun `a returning user with a provider opens home`() = runTest {
        every { mockSettingsRepository.observeSettings() } returns
            flowOf(AppSettings(isFirstRun = false))
        val viewModel = createViewModel()
        val actual = viewModel.startOnWelcome.filterNotNull().first()
        assertFalse(actual)
    }

    @Test
    fun `a returning user without a provider opens welcome`() = runTest {
        every { mockSettingsRepository.observeSettings() } returns
            flowOf(AppSettings(isFirstRun = false))
        coEvery { mockHasAvailableLlmProvider() } returns false
        val viewModel = createViewModel()
        val actual = viewModel.startOnWelcome.filterNotNull().first()
        assertTrue(actual)
    }

    @Test
    fun `theme starts as system until settings are loaded`() {
        every { mockSettingsRepository.observeSettings() } returns flowOf()
        val viewModel = createViewModel()
        assertEquals(AppTheme.SYSTEM, viewModel.theme.value)
    }

    @Test
    fun `theme follows the stored preference`() = runTest {
        every { mockSettingsRepository.observeSettings() } returns
            flowOf(AppSettings(theme = AppTheme.DARK))
        val viewModel = createViewModel()
        val actual = viewModel.theme.first { theme -> theme == AppTheme.DARK }
        assertEquals(AppTheme.DARK, actual)
    }

    private fun createViewModel() = MainViewModel(
        settingsRepository = mockSettingsRepository,
        hasAvailableLlmProvider = mockHasAvailableLlmProvider,
    )
}
