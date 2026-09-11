package com.vitorfg8.quizia.core.llm.prompt

import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.core.domain.model.QuizRequest
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class QuizPromptBuilderTest {

    private val clock: Clock = Clock.fixed(Instant.parse("2026-09-11T12:00:00Z"), ZoneOffset.UTC)
    private val builder: QuizPromptBuilder = QuizPromptBuilder(clock)

    @Test
    fun `the prompt states the requested question count`() {
        val actual = builder.build(buildRequest(questionCount = 15))
        assertTrue(actual.contains("exactly 15 multiple choice questions"))
    }

    @Test
    fun `the prompt names the category topic`() {
        val actual = builder.build(buildRequest(category = QuizCategory.MOVIES_AND_TV))
        assertTrue(actual.contains("movies and TV series"))
    }

    @Test
    fun `every category maps to a topic`() {
        val actual = QuizCategory.entries.map { category ->
            builder.build(buildRequest(category = category))
        }
        assertTrue(actual.none { it.contains("_") })
    }

    @Test
    fun `a portuguese request asks for brazilian portuguese`() {
        val actual = builder.build(buildRequest(language = "pt"))
        assertTrue(actual.contains("Brazilian Portuguese"))
    }

    @Test
    fun `any other language falls back to english`() {
        val actual = builder.build(buildRequest(language = "fr"))
        assertTrue(actual.contains("English"))
    }

    @Test
    fun `current events are limited to recent global news`() {
        val actual: String = builder.build(buildRequest(category = QuizCategory.CURRENT_EVENTS))
        assertTrue(actual.contains("current events"))
        assertTrue(actual.contains("last 12 months"))
        assertTrue(actual.contains("widely reported global news"))
        assertTrue(actual.contains("Today is 2026-09-11."))
    }

    @Test
    fun `current events use the date from the system clock`() {
        val inputClock: Clock = Clock.fixed(
            Instant.parse("2031-01-15T00:00:00Z"),
            ZoneOffset.UTC,
        )
        val actual: String = QuizPromptBuilder(inputClock)
            .build(buildRequest(category = QuizCategory.CURRENT_EVENTS))
        assertTrue(actual.contains("Today is 2031-01-15."))
    }

    @Test
    fun `other categories do not mention the current events window`() {
        val actual: String = builder.build(buildRequest(category = QuizCategory.NATURE))
        assertTrue(actual.contains("nature and the living world"))
        assertTrue(!actual.contains("last 12 months"))
        assertTrue(!actual.contains("Today is"))
    }

    @Test
    fun `the prompt describes the json contract`() {
        val actual = builder.build(buildRequest())
        assertTrue(actual.contains("\"correctIndex\""))
        assertTrue(actual.contains("\"explanation\""))
        assertTrue(actual.contains("exactly 4 options"))
        assertTrue(actual.contains("one-sentence explanation"))
    }

    private fun buildRequest(
        category: QuizCategory = QuizCategory.GENERAL_KNOWLEDGE,
        questionCount: Int = 5,
        language: String = "en",
    ) = QuizRequest(category = category, questionCount = questionCount, language = language)
}
