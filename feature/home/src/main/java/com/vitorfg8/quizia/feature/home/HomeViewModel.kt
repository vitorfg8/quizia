package com.vitorfg8.quizia.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.core.domain.usecase.HasAvailableLlmProviderUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val hasAvailableLlmProvider: HasAvailableLlmProviderUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<HomeSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<HomeSideEffect> = _sideEffect.receiveAsFlow()

    fun ensureProviderAvailable() {
        viewModelScope.launch {
            if (!hasAvailableLlmProvider()) {
                _sideEffect.send(HomeSideEffect.NavigateToWelcome)
            }
        }
    }

    fun onCategoryClick(category: QuizCategory) {
        viewModelScope.launch {
            _sideEffect.send(HomeSideEffect.NavigateToQuiz(category))
        }
    }

    fun onSettingsClick() {
        viewModelScope.launch {
            _sideEffect.send(HomeSideEffect.NavigateToSettings)
        }
    }
}
