package com.vitorfg8.quizia.feature.results

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorfg8.quizia.core.domain.usecase.CalculateStarRatingUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ResultsViewModel(
    score: Int,
    total: Int,
    calculateStarRating: CalculateStarRatingUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(buildInitialState(score, total, calculateStarRating))
    val uiState: StateFlow<ResultsUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<ResultsSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<ResultsSideEffect> = _sideEffect.receiveAsFlow()

    fun onBackToHomeClick() {
        viewModelScope.launch {
            _sideEffect.send(ResultsSideEffect.NavigateToHome)
        }
    }
}

private fun buildInitialState(
    score: Int,
    total: Int,
    calculateStarRating: CalculateStarRatingUseCase,
): ResultsUiState {
    val stars = calculateStarRating(score = score, total = total)
    return ResultsUiState(
        score = score,
        total = total,
        stars = stars,
        performanceMessageResId = stars.toPerformanceMessageResId(),
    )
}

/** Message for each star count, indexed from zero stars up to a perfect score. */
private val PERFORMANCE_MESSAGES = listOf(
    R.string.results_message_zero_stars,
    R.string.results_message_one_star,
    R.string.results_message_two_stars,
    R.string.results_message_three_stars,
    R.string.results_message_four_stars,
    R.string.results_message_five_stars,
)

@StringRes
private fun Int.toPerformanceMessageResId(): Int =
    PERFORMANCE_MESSAGES[coerceIn(PERFORMANCE_MESSAGES.indices)]
