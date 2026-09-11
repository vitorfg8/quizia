package com.vitorfg8.quizia.feature.results

data class ResultsUiState(
    val score: Int = 0,
    val total: Int = 0,
    val elapsedMs: Long = 0L,
) {
    val wrongCount: Int get() = (total - score).coerceAtLeast(0)
    val isSuccess: Boolean get() = total > 0 && score * 2 >= total
}
