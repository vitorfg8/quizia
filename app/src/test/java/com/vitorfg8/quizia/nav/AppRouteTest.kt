package com.vitorfg8.quizia.nav

import com.vitorfg8.quizia.core.domain.model.QuizCategory
import org.junit.Assert.assertEquals
import org.junit.Test

class AppRouteTest {

    @Test
    fun `the quiz route carries the category name`() {
        val actual = AppRoute.buildQuizRoute(QuizCategory.ASTRONOMY.name)
        assertEquals("quiz/ASTRONOMY", actual)
    }

    @Test
    fun `the results route carries the score and the total`() {
        val actual = AppRoute.buildResultsRoute(score = 3, total = 5)
        assertEquals("results/3/5", actual)
    }

    @Test
    fun `a built quiz route matches the declared pattern`() {
        val actual = AppRoute.QUIZ.replace("{${AppRoute.ARG_CATEGORY}}", QuizCategory.SPORTS.name)
        assertEquals(AppRoute.buildQuizRoute(QuizCategory.SPORTS.name), actual)
    }

    @Test
    fun `a known category name is parsed back into the enum`() {
        val actual = QuizCategory.MOVIES_AND_TV.name.toQuizCategory()
        assertEquals(QuizCategory.MOVIES_AND_TV, actual)
    }

    @Test
    fun `an unknown category name falls back to general knowledge`() {
        val actual = "PHILOSOPHY".toQuizCategory()
        assertEquals(QuizCategory.GENERAL_KNOWLEDGE, actual)
    }

    @Test
    fun `a missing category argument falls back to general knowledge`() {
        val actual = null.toQuizCategory()
        assertEquals(QuizCategory.GENERAL_KNOWLEDGE, actual)
    }
}
