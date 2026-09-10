package com.vitorfg8.quizia.nav

internal object AppRoute {
    const val WELCOME = "welcome"
    const val LLM_SETUP = "llm_setup"
    const val HOME = "home"

    const val ARG_CATEGORY = "category"
    const val ARG_SCORE = "score"
    const val ARG_TOTAL = "total"

    const val QUIZ = "quiz/{$ARG_CATEGORY}"
    const val RESULTS = "results/{$ARG_SCORE}/{$ARG_TOTAL}"

    fun buildQuizRoute(category: String): String = "quiz/$category"

    fun buildResultsRoute(score: Int, total: Int): String = "results/$score/$total"
}
