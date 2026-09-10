package com.vitorfg8.quizia.feature.llmsetup

import com.vitorfg8.quizia.core.domain.model.LlmProviderType

data class LlmSetupUiState(
    val availableProviders: List<LlmProviderType> = emptyList(),
    val selectedProvider: LlmProviderType? = null,
    val apiKey: String = "",
    val isApiKeyRequired: Boolean = false,
    val canContinue: Boolean = false,
)
