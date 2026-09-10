package com.vitorfg8.quizia.feature.llmsetup

sealed interface LlmSetupSideEffect {
    data object NavigateToHome : LlmSetupSideEffect
}
