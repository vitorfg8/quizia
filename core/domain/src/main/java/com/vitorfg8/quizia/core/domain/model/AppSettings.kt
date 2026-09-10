package com.vitorfg8.quizia.core.domain.model

data class AppSettings(
    val selectedProvider: LlmProviderType = LlmProviderType.GEMINI_API,
    val theme: AppTheme = AppTheme.SYSTEM,
    val questionCount: Int = 5,
    val isFirstRun: Boolean = true,
)
