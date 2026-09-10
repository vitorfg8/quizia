package com.vitorfg8.quizia.core.llm.prompt

import com.vitorfg8.quizia.core.domain.model.Question
import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.core.domain.model.QuizRequest

/**
 * Builds the prompt every provider sends. The response contract is identical across providers
 * so a single [com.vitorfg8.quizia.core.llm.parser.QuizJsonParser] can read all of them.
 */
class QuizPromptBuilder {

    fun build(request: QuizRequest): String = buildString {
        appendLine("You are a quiz generator.")
        appendLine(
            "Generate exactly ${request.questionCount} multiple choice questions about " +
                "${request.category.toPromptTopic()}.",
        )
        appendLine("Write every question and every option in ${request.language.toLanguageName()}.")
        appendLine("Each question must have exactly ${Question.OPTIONS_PER_QUESTION} options.")
        appendLine("Exactly one option must be correct.")
        appendLine("Vary the position of the correct option across questions.")
        appendLine("Respond with raw JSON only, with no markdown fence and no text around it.")
        appendLine("Use exactly this shape:")
        append(JSON_CONTRACT)
    }

    private fun QuizCategory.toPromptTopic(): String = when (this) {
        QuizCategory.GENERAL_KNOWLEDGE -> "general knowledge"
        QuizCategory.HISTORY_AND_GEOGRAPHY -> "history and geography"
        QuizCategory.INTERNATIONAL_MUSIC -> "international music"
        QuizCategory.MOVIES_AND_TV -> "movies and TV series"
        QuizCategory.SPORTS -> "sports"
        QuizCategory.ASTRONOMY -> "astronomy"
    }

    private fun String.toLanguageName(): String = when (this) {
        PORTUGUESE_TAG -> "Brazilian Portuguese"
        else -> "English"
    }

    private companion object {
        const val PORTUGUESE_TAG = "pt"
        val JSON_CONTRACT = """
            {"questions":[{"text":"...","options":["...","...","...","..."],"correctIndex":0}]}
        """.trimIndent()
    }
}
