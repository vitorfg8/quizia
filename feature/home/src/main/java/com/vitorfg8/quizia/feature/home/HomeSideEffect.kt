package com.vitorfg8.quizia.feature.home

import com.vitorfg8.quizia.core.domain.model.QuizCategory

sealed interface HomeSideEffect {
    data class NavigateToQuiz(val category: QuizCategory) : HomeSideEffect
    data object NavigateToSettings : HomeSideEffect
}
