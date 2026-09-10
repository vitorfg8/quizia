package com.vitorfg8.quizia.core.llm.prompt

import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.core.domain.model.QuizRequest
import org.junit.Assert.assertTrue
import org.junit.Test

class QuizPromptBuilderTest {

    private val builder = QuizPromptBuilder()

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
    fun `the prompt describes the json contract`() {
        val actual = builder.build(buildRequest())
        assertTrue(actual.contains("\"correctIndex\""))
        assertTrue(actual.contains("exactly 4 options"))
    }

    private fun buildRequest(
        category: QuizCategory = QuizCategory.GENERAL_KNOWLEDGE,
        questionCount: Int = 5,
        language: String = "en",
    ) = QuizRequest(category = category, questionCount = questionCount, language = language)
}
