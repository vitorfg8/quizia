package com.vitorfg8.quizia.feature.results

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ResultsViewModelTest {

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
        val actual = createViewModel(score = 3, total = 5).uiState.value
        assertEquals(3, actual.score)
        assertEquals(5, actual.total)
        assertEquals(2, actual.wrongCount)
    }

    @Test
    fun `a majority of correct answers is a success`() {
        val actual = createViewModel(score = 8, total = 10).uiState.value
        assertTrue(actual.isSuccess)
    }

    @Test
    fun `a quiz with no questions is not a success`() {
        val actual = createViewModel(score = 0, total = 0).uiState.value
        assertFalse(actual.isSuccess)
    }

    @Test
    fun `fewer than half the answers is a failure`() {
        val actual = createViewModel(score = 4, total = 10).uiState.value
        assertFalse(actual.isSuccess)
    }

    @Test
    fun `a score of eighty percent or more celebrates`() {
        val actual = createViewModel(score = 8, total = 10).uiState.value
        assertTrue(actual.shouldCelebrate)
    }

    @Test
    fun `a score below eighty percent does not celebrate`() {
        val actual = createViewModel(score = 7, total = 10).uiState.value
        assertFalse(actual.shouldCelebrate)
    }

    @Test
    fun `a quiz with no questions does not celebrate`() {
        val actual = createViewModel(score = 0, total = 0).uiState.value
        assertFalse(actual.shouldCelebrate)
    }

    @Test
    fun `the elapsed time is shown as it was received`() {
        val actual = createViewModel(score = 8, total = 10, elapsedMs = INPUT_ELAPSED_MS).uiState.value
        assertEquals(INPUT_ELAPSED_MS, actual.elapsedMs)
    }

    @Test
    fun `tapping play another emits the quiz navigation effect`() = runTest {
        val viewModel = createViewModel(score = 8, total = 10)
        viewModel.onPlayAnotherClick()
        val actual = viewModel.sideEffect.first()
        assertEquals(ResultsSideEffect.NavigateToQuiz, actual)
    }

    @Test
    fun `tapping back emits the home navigation effect`() = runTest {
        val viewModel = createViewModel(score = 3, total = 5)
        viewModel.onBackToHomeClick()
        val actual = viewModel.sideEffect.first()
        assertEquals(ResultsSideEffect.NavigateToHome, actual)
    }

    private fun createViewModel(
        score: Int,
        total: Int,
        elapsedMs: Long = 0L,
    ) = ResultsViewModel(score = score, total = total, elapsedMs = elapsedMs)

    private companion object {
        const val INPUT_ELAPSED_MS = 392_000L
    }
}
