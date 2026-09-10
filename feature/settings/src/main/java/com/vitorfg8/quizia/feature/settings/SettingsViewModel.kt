package com.vitorfg8.quizia.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitorfg8.quizia.core.domain.model.AppTheme
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.repository.ApiKeyRepository
import com.vitorfg8.quizia.core.domain.repository.OnDeviceModelRepository
import com.vitorfg8.quizia.core.domain.repository.SettingsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val apiKeyRepository: ApiKeyRepository,
    private val onDeviceModelRepository: OnDeviceModelRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<SettingsSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<SettingsSideEffect> = _sideEffect.receiveAsFlow()

    init {
        observeSettings()
    }

    fun selectProvider(provider: LlmProviderType) {
        applyProviderSelection(provider)
        viewModelScope.launch { settingsRepository.saveSelectedProvider(provider) }
    }

    fun selectTheme(theme: AppTheme) {
        _uiState.update { it.copy(theme = theme) }
        viewModelScope.launch { settingsRepository.saveTheme(theme) }
    }

    fun selectQuestionCount(count: Int) {
        _uiState.update { it.copy(questionCount = count) }
        viewModelScope.launch { settingsRepository.saveQuestionCount(count) }
    }

    fun updateDraftApiKey(key: String) {
        _uiState.update { it.copy(draftApiKey = key) }
    }

    fun onChangeApiKeyClick() {
        _uiState.update { it.copy(isEditingApiKey = true, draftApiKey = "") }
    }

    fun onDeleteApiKeyClick() {
        if (!_uiState.value.hasStoredApiKey) return
        _uiState.update { it.copy(isDeleteKeyDialogVisible = true) }
    }

    fun dismissDeleteApiKeyDialog() {
        _uiState.update { it.copy(isDeleteKeyDialogVisible = false) }
    }

    fun confirmDeleteApiKey() {
        val state = _uiState.value
        if (!state.hasStoredApiKey) return
        viewModelScope.launch {
            apiKeyRepository.clearApiKey(state.selectedProvider)
            _uiState.update {
                it.copy(
                    apiKeyMasked = "",
                    isEditingApiKey = true,
                    draftApiKey = "",
                    isDeleteKeyDialogVisible = false,
                )
            }
        }
    }

    fun saveApiKey() {
        val state = _uiState.value
        if (!state.canSaveApiKey) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            apiKeyRepository.saveApiKey(state.selectedProvider, state.draftApiKey)
            _uiState.update {
                it.copy(
                    isSaving = false,
                    apiKeyMasked = maskApiKey(state.draftApiKey),
                    isEditingApiKey = false,
                    draftApiKey = "",
                )
            }
        }
    }

    fun onBackClick() {
        viewModelScope.launch {
            _sideEffect.send(SettingsSideEffect.NavigateBack)
        }
    }

    private fun observeSettings() {
        viewModelScope.launch {
            val providers = buildProviderList(onDeviceModelRepository.isAvailable())
            settingsRepository.observeSettings().collect { settings ->
                val current = _uiState.value
                val shouldResetKeyEditor = current.availableProviders.isEmpty() ||
                    current.selectedProvider != settings.selectedProvider
                if (shouldResetKeyEditor) {
                    applyProviderSelection(
                        provider = settings.selectedProvider,
                        theme = settings.theme,
                        questionCount = settings.questionCount,
                        availableProviders = providers,
                    )
                } else {
                    _uiState.update {
                        it.copy(
                            theme = settings.theme,
                            questionCount = settings.questionCount,
                            availableProviders = providers,
                        )
                    }
                }
            }
        }
    }

    private fun applyProviderSelection(
        provider: LlmProviderType,
        theme: AppTheme = _uiState.value.theme,
        questionCount: Int = _uiState.value.questionCount,
        availableProviders: List<LlmProviderType> = _uiState.value.availableProviders,
    ) {
        val storedKey = apiKeyRepository.getApiKey(provider)
        val isApiKeyRequired = provider != LlmProviderType.GEMINI_NANO
        _uiState.update { current ->
            current.copy(
                selectedProvider = provider,
                apiKeyMasked = maskApiKey(storedKey),
                theme = theme,
                questionCount = questionCount,
                availableProviders = availableProviders,
                isApiKeyRequired = isApiKeyRequired,
                isEditingApiKey = isApiKeyRequired && storedKey.isNullOrBlank(),
                draftApiKey = "",
                isDeleteKeyDialogVisible = false,
            )
        }
    }
}

internal fun buildProviderList(isGeminiNanoSupported: Boolean): List<LlmProviderType> =
    buildList {
        if (isGeminiNanoSupported) add(LlmProviderType.GEMINI_NANO)
        add(LlmProviderType.GEMINI_API)
        add(LlmProviderType.OPENAI)
        add(LlmProviderType.CLAUDE)
    }
