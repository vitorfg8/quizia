package com.vitorfg8.quizia.feature.home

import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.core.domain.usecase.HasAvailableLlmProviderUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val mockHasAvailableLlmProvider = mockk<HasAvailableLlmProviderUseCase>()
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        coEvery { mockHasAvailableLlmProvider() } returns true
        viewModel = HomeViewModel(hasAvailableLlmProvider = mockHasAvailableLlmProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state exposes every quiz category`() {
        val expected = QuizCategory.entries
        val actual = viewModel.uiState.value.categories
        assertEquals(expected, actual)
    }

    @Test
    fun `clicking a category navigates to its quiz`() = runTest {
        viewModel.onCategoryClick(QuizCategory.ASTRONOMY)
        val expected = HomeSideEffect.NavigateToQuiz(QuizCategory.ASTRONOMY)
        val actual = viewModel.sideEffect.first()
        assertEquals(expected, actual)
    }

    @Test
    fun `clicking settings navigates to settings`() = runTest {
        viewModel.onSettingsClick()
        val actual = viewModel.sideEffect.first()
        assertEquals(HomeSideEffect.NavigateToSettings, actual)
    }

    @Test
    fun `a missing provider sends the user back to welcome`() = runTest {
        coEvery { mockHasAvailableLlmProvider() } returns false
        viewModel.ensureProviderAvailable()
        val actual = viewModel.sideEffect.first()
        assertEquals(HomeSideEffect.NavigateToWelcome, actual)
    }
}
