package com.vitorfg8.quizia.feature.home

import com.vitorfg8.quizia.core.domain.model.QuizCategory

data class HomeUiState(
    val categories: List<QuizCategory> = QuizCategory.entries,
)
