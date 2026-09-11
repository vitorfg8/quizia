package com.vitorfg8.quizia.core.llm.parser

import com.google.gson.Gson
import com.vitorfg8.quizia.core.domain.model.Question
import com.vitorfg8.quizia.core.domain.model.QuizResponse

/**
 * Reads the JSON contract described by
 * [com.vitorfg8.quizia.core.llm.prompt.QuizPromptBuilder] out of a raw model response.
 *
 * Models routinely wrap the payload in a markdown fence or add a sentence around it despite
 * being told not to, so the object is located by its braces rather than by parsing the whole
 * response. Anything that does not satisfy the contract fails instead of reaching the UI.
 */
class QuizJsonParser(
    private val gson: Gson,
) {

    fun parse(rawResponse: String): Result<QuizResponse> = runCatching {
        val payload = gson.fromJson(rawResponse.extractJsonObject(), QuizPayloadDto::class.java)
        val questions = payload?.questions.orEmpty().map { it.toQuestion() }
        require(questions.isNotEmpty()) { "The model returned no questions." }
        QuizResponse(questions)
    }

    private fun String.extractJsonObject(): String {
        val start = indexOf('{')
        val end = lastIndexOf('}')
        require(start >= 0 && end > start) { "The model response contains no JSON object." }
        return substring(start, end + 1)
    }

    private fun QuestionDto.toQuestion(): Question {
        val questionText = text?.takeIf { it.isNotBlank() }
        requireNotNull(questionText) { "A question has no text." }
        val validOptions = options.orEmpty().filterNotNull().filter { it.isNotBlank() }
        require(validOptions.size == Question.OPTIONS_PER_QUESTION) {
            "A question has ${validOptions.size} options instead of ${Question.OPTIONS_PER_QUESTION}."
        }
        val index = correctIndex
        requireNotNull(index) { "A question has no correct option." }
        require(index in validOptions.indices) { "The correct option index $index is out of range." }
        return Question(
            text = questionText,
            options = validOptions,
            correctIndex = index,
            explanation = explanation.orEmpty().trim(),
        )
    }
}
