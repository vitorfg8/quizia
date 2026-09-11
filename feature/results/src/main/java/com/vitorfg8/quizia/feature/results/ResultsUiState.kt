package com.vitorfg8.quizia.feature.results

data class ResultsUiState(
    val score: Int = 0,
    val total: Int = 0,
    val elapsedMs: Long = 0L,
) {
    val wrongCount: Int get() = (total - score).coerceAtLeast(0)
    val isSuccess: Boolean get() = total > 0 && score * 2 >= total
    val shouldCelebrate: Boolean
        get() = total > 0 && score * PERCENT_BASE >= total * CELEBRATION_PERCENT

    private companion object {
        const val PERCENT_BASE = 100
        const val CELEBRATION_PERCENT = 80
    }
}
