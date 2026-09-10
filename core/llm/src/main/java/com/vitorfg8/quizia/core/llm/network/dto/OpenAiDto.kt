package com.vitorfg8.quizia.core.llm.network.dto

import com.google.gson.annotations.SerializedName

internal data class OpenAiRequestDto(
    @SerializedName("model") val model: String,
    @SerializedName("messages") val messages: List<OpenAiMessageDto>,
    @SerializedName("response_format") val responseFormat: OpenAiResponseFormatDto,
)

internal data class OpenAiMessageDto(
    @SerializedName("role") val role: String? = null,
    @SerializedName("content") val content: String? = null,
)

internal data class OpenAiResponseFormatDto(
    @SerializedName("type") val type: String,
)

internal data class OpenAiResponseDto(
    @SerializedName("choices") val choices: List<OpenAiChoiceDto>? = null,
)

internal data class OpenAiChoiceDto(
    @SerializedName("message") val message: OpenAiMessageDto? = null,
)
