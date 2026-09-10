package com.vitorfg8.quizia.feature.llmsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.repository.ApiKeyRepository
import com.vitorfg8.quizia.core.domain.repository.SettingsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LlmSetupViewModel(
    private val settingsRepository: SettingsRepository,
    private val apiKeyRepository: ApiKeyRepository,
    private val isGeminiNanoSupported: Boolean,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        LlmSetupUiState(availableProviders = buildProviderList(isGeminiNanoSupported)),
    )
    val uiState: StateFlow<LlmSetupUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<LlmSetupSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<LlmSetupSideEffect> = _sideEffect.receiveAsFlow()

    fun selectProvider(provider: LlmProviderType) {
        _uiState.update { state ->
            val isApiKeyRequired = provider != LlmProviderType.GEMINI_NANO
            state.copy(
                selectedProvider = provider,
                isApiKeyRequired = isApiKeyRequired,
                canContinue = resolveCanContinue(provider, state.apiKey),
            )
        }
    }

    fun updateApiKey(key: String) {
        _uiState.update { state ->
            state.copy(
                apiKey = key,
                canContinue = resolveCanContinue(state.selectedProvider, key),
            )
        }
    }

    fun onContinueClick() {
        val state = _uiState.value
        val provider = state.selectedProvider ?: return
        viewModelScope.launch {
            if (state.isApiKeyRequired && state.apiKey.isNotBlank()) {
                apiKeyRepository.saveApiKey(provider, state.apiKey)
            }
            settingsRepository.saveSelectedProvider(provider)
            settingsRepository.setFirstRunComplete()
            _sideEffect.send(LlmSetupSideEffect.NavigateToHome)
        }
    }

    private fun resolveCanContinue(provider: LlmProviderType?, apiKey: String): Boolean {
        if (provider == null) return false
        if (provider == LlmProviderType.GEMINI_NANO) return true
        return apiKey.isNotBlank()
    }
}

private fun buildProviderList(isGeminiNanoSupported: Boolean): List<LlmProviderType> =
    buildList {
        if (isGeminiNanoSupported) add(LlmProviderType.GEMINI_NANO)
        add(LlmProviderType.GEMINI_API)
        add(LlmProviderType.OPENAI)
        add(LlmProviderType.CLAUDE)
    }
