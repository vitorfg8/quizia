package com.vitorfg8.quizia.core.llm.parser

import com.google.gson.annotations.SerializedName

internal data class QuizPayloadDto(
    @SerializedName("questions") val questions: List<QuestionDto>? = null,
)

internal data class QuestionDto(
    @SerializedName("text") val text: String? = null,
    @SerializedName("options") val options: List<String?>? = null,
    @SerializedName("correctIndex") val correctIndex: Int? = null,
)
