package com.vitorfg8.quizia.core.domain.model

/**
 * A single quiz question with exactly [OPTIONS_PER_QUESTION] alternatives.
 * [correctIndex] points at the right entry of [options].
 */
data class Question(
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
) {
    val correctOption: String get() = options[correctIndex]

    companion object {
        const val OPTIONS_PER_QUESTION = 4
    }
}
