package com.vitorfg8.quizia.core.llm.parser

import com.google.gson.Gson
import com.vitorfg8.quizia.core.domain.model.Question
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class QuizJsonParserTest {

    private val parser = QuizJsonParser(Gson())

    @Test
    fun `a well formed payload becomes questions`() {
        val actual = parser.parse(VALID_PAYLOAD).getOrThrow()
        val expected = Question(
            text = "Largest planet?",
            options = listOf("Mars", "Jupiter", "Venus", "Mercury"),
            correctIndex = 1,
            explanation = "Jupiter is the largest planet in the Solar System.",
        )
        assertEquals(listOf(expected), actual.questions)
    }

    @Test
    fun `a payload wrapped in a markdown fence is still read`() {
        val inputResponse = "```json\n$VALID_PAYLOAD\n```"
        val actual = parser.parse(inputResponse).getOrThrow()
        assertEquals(1, actual.questions.size)
    }

    @Test
    fun `prose around the payload is ignored`() {
        val inputResponse = "Sure! Here is your quiz:\n$VALID_PAYLOAD\nHope you enjoy it."
        val actual = parser.parse(inputResponse).getOrThrow()
        assertEquals(1, actual.questions.size)
    }

    @Test
    fun `a response without any json object fails`() {
        val actual = parser.parse("I cannot help with that.")
        assertTrue(actual.isFailure)
    }

    @Test
    fun `malformed json fails`() {
        val actual = parser.parse("{\"questions\": [")
        assertTrue(actual.isFailure)
    }

    @Test
    fun `an empty question list fails`() {
        val actual = parser.parse("{\"questions\":[]}")
        assertTrue(actual.isFailure)
    }

    @Test
    fun `a question without text fails`() {
        val inputResponse = """
            {"questions":[{"text":"  ","options":["a","b","c","d"],"correctIndex":0}]}
        """.trimIndent()
        val actual = parser.parse(inputResponse)
        assertTrue(actual.isFailure)
    }

    @Test
    fun `a question with the wrong number of options fails`() {
        val inputResponse = """
            {"questions":[{"text":"Largest planet?","options":["Mars","Jupiter"],"correctIndex":0}]}
        """.trimIndent()
        val actual = parser.parse(inputResponse)
        assertTrue(actual.isFailure)
    }

    @Test
    fun `a question with a blank option fails`() {
        val inputResponse = """
            {"questions":[{"text":"Largest planet?","options":["Mars","","Venus","Mercury"],"correctIndex":0}]}
        """.trimIndent()
        val actual = parser.parse(inputResponse)
        assertTrue(actual.isFailure)
    }

    @Test
    fun `a question without a correct index fails`() {
        val inputResponse = """
            {"questions":[{"text":"Largest planet?","options":["a","b","c","d"]}]}
        """.trimIndent()
        val actual = parser.parse(inputResponse)
        assertTrue(actual.isFailure)
    }

    @Test
    fun `a correct index outside the options fails`() {
        val inputResponse = """
            {"questions":[{"text":"Largest planet?","options":["a","b","c","d"],"correctIndex":7}]}
        """.trimIndent()
        val actual = parser.parse(inputResponse)
        assertTrue(actual.isFailure)
    }

    @Test
    fun `a payload without the questions field fails`() {
        val actual = parser.parse("{\"result\":\"none\"}")
        assertTrue(actual.isFailure)
    }

    @Test
    fun `a missing explanation still becomes a question`() {
        val inputResponse = """
            {"questions":[{"text":"Largest planet?","options":["Mars","Jupiter","Venus","Mercury"],"correctIndex":1}]}
        """.trimIndent()
        val actual = parser.parse(inputResponse).getOrThrow()
        val expected = Question(
            text = "Largest planet?",
            options = listOf("Mars", "Jupiter", "Venus", "Mercury"),
            correctIndex = 1,
            explanation = "",
        )
        assertEquals(listOf(expected), actual.questions)
    }

    private companion object {
        val VALID_PAYLOAD = """
            {"questions":[{"text":"Largest planet?","options":["Mars","Jupiter","Venus","Mercury"],"correctIndex":1,"explanation":"Jupiter is the largest planet in the Solar System."}]}
        """.trimIndent()
    }
}
