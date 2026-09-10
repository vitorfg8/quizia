package com.vitorfg8.quizia.feature.results

import com.vitorfg8.quizia.core.domain.usecase.CalculateStarRatingUseCase
import io.mockk.every
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
class ResultsViewModelTest {

    private val mockCalculateStarRating = mockk<CalculateStarRatingUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `the score is shown as it was received`() {
        givenStarRating(3)
        val actual = createViewModel(score = 3, total = 5).uiState.value
        assertEquals(3, actual.score)
        assertEquals(5, actual.total)
    }

    @Test
    fun `the star rating comes from the use case`() {
        givenStarRating(4)
        val actual = createViewModel(score = 4, total = 5).uiState.value
        assertEquals(4, actual.stars)
    }

    @Test
    fun `no star shows the encouraging message`() {
        givenStarRating(0)
        val actual = createViewModel(score = 0, total = 5).uiState.value
        assertEquals(R.string.results_message_zero_stars, actual.performanceMessageResId)
    }

    @Test
    fun `one star shows its own message`() {
        givenStarRating(1)
        val actual = createViewModel(score = 1, total = 5).uiState.value
        assertEquals(R.string.results_message_one_star, actual.performanceMessageResId)
    }

    @Test
    fun `two stars show their own message`() {
        givenStarRating(2)
        val actual = createViewModel(score = 2, total = 5).uiState.value
        assertEquals(R.string.results_message_two_stars, actual.performanceMessageResId)
    }

    @Test
    fun `three stars show their own message`() {
        givenStarRating(3)
        val actual = createViewModel(score = 3, total = 5).uiState.value
        assertEquals(R.string.results_message_three_stars, actual.performanceMessageResId)
    }

    @Test
    fun `four stars show their own message`() {
        givenStarRating(4)
        val actual = createViewModel(score = 4, total = 5).uiState.value
        assertEquals(R.string.results_message_four_stars, actual.performanceMessageResId)
    }

    @Test
    fun `five stars show the perfect score message`() {
        givenStarRating(5)
        val actual = createViewModel(score = 5, total = 5).uiState.value
        assertEquals(R.string.results_message_five_stars, actual.performanceMessageResId)
    }

    @Test
    fun `tapping back emits the navigation effect`() = runTest {
        givenStarRating(3)
        val viewModel = createViewModel(score = 3, total = 5)
        viewModel.onBackToHomeClick()
        val actual = viewModel.sideEffect.first()
        assertEquals(ResultsSideEffect.NavigateToHome, actual)
    }

    private fun givenStarRating(stars: Int) {
        every { mockCalculateStarRating(any(), any()) } returns stars
    }

    private fun createViewModel(score: Int, total: Int) = ResultsViewModel(
        score = score,
        total = total,
        calculateStarRating = mockCalculateStarRating,
    )
}
