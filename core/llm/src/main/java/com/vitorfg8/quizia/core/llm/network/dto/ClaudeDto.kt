package com.vitorfg8.quizia.core.llm.network.dto

import com.google.gson.annotations.SerializedName

internal data class ClaudeRequestDto(
    @SerializedName("model") val model: String,
    @SerializedName("max_tokens") val maxTokens: Int,
    @SerializedName("messages") val messages: List<ClaudeMessageDto>,
)

internal data class ClaudeMessageDto(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String,
)

internal data class ClaudeResponseDto(
    @SerializedName("content") val content: List<ClaudeContentDto>? = null,
)

internal data class ClaudeContentDto(
    @SerializedName("type") val type: String? = null,
    @SerializedName("text") val text: String? = null,
)
