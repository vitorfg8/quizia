package com.vitorfg8.quizia.feature.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    elapsedMs: Long,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ResultsUiState(score = score, total = total, elapsedMs = elapsedMs),
    )
    val uiState: StateFlow<ResultsUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<ResultsSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<ResultsSideEffect> = _sideEffect.receiveAsFlow()

    fun onPlayAnotherClick() {
        viewModelScope.launch {
            _sideEffect.send(ResultsSideEffect.NavigateToQuiz)
        }
    }

    fun onBackToHomeClick() {
        viewModelScope.launch {
            _sideEffect.send(ResultsSideEffect.NavigateToHome)
        }
    }
}
