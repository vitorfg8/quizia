package com.vitorfg8.quizia.nav

internal object AppRoute {
    const val WELCOME = "welcome"
    const val LLM_SETUP = "llm_setup"
    const val HOME = "home"
    const val SETTINGS = "settings"

    const val ARG_CATEGORY = "category"
    const val ARG_SCORE = "score"
    const val ARG_TOTAL = "total"
    const val ARG_ELAPSED_MS = "elapsedMs"

    const val QUIZ = "quiz/{$ARG_CATEGORY}"
    const val RESULTS = "results/{$ARG_CATEGORY}/{$ARG_SCORE}/{$ARG_TOTAL}/{$ARG_ELAPSED_MS}"

    fun buildQuizRoute(category: String): String = "quiz/$category"

    fun buildResultsRoute(
        category: String,
        score: Int,
        total: Int,
        elapsedMs: Long,
    ): String = "results/$category/$score/$total/$elapsedMs"
}
