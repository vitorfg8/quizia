package com.vitorfg8.quizia.feature.results

sealed interface ResultsSideEffect {
    data object NavigateToQuiz : ResultsSideEffect
    data object NavigateToHome : ResultsSideEffect
}
