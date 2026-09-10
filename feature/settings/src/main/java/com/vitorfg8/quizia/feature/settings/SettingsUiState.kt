package com.vitorfg8.quizia.feature.settings

import com.vitorfg8.quizia.core.domain.model.AppTheme
import com.vitorfg8.quizia.core.domain.model.LlmProviderType

data class SettingsUiState(
    val selectedProvider: LlmProviderType = LlmProviderType.GEMINI_API,
    val apiKeyMasked: String = "",
    val theme: AppTheme = AppTheme.SYSTEM,
    val questionCount: Int = DEFAULT_QUESTION_COUNT,
    val availableProviders: List<LlmProviderType> = emptyList(),
    val isSaving: Boolean = false,
    val isApiKeyRequired: Boolean = true,
    val isEditingApiKey: Boolean = false,
    val draftApiKey: String = "",
    val isDeleteKeyDialogVisible: Boolean = false,
) {
    val canSaveApiKey: Boolean get() = isApiKeyRequired && draftApiKey.isNotBlank() && !isSaving
    val hasStoredApiKey: Boolean get() = apiKeyMasked.isNotEmpty()
}

internal const val DEFAULT_QUESTION_COUNT = 5
internal val QUESTION_COUNT_OPTIONS = listOf(5, 10, 15)
