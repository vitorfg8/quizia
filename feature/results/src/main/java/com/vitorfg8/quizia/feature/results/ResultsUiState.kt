package com.vitorfg8.quizia.feature.results

import androidx.annotation.StringRes

data class ResultsUiState(
    val score: Int = 0,
    val total: Int = 0,
    val stars: Int = 0,
    @StringRes val performanceMessageResId: Int = R.string.results_message_zero_stars,
)
