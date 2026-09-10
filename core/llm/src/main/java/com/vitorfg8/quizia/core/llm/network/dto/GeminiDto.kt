package com.vitorfg8.quizia.core.llm.network.dto

import com.google.gson.annotations.SerializedName

internal data class GeminiRequestDto(
    @SerializedName("contents") val contents: List<GeminiContentDto>,
    @SerializedName("generationConfig") val generationConfig: GeminiGenerationConfigDto,
)

internal data class GeminiContentDto(
    @SerializedName("parts") val parts: List<GeminiPartDto>? = null,
)

internal data class GeminiPartDto(
    @SerializedName("text") val text: String? = null,
)

internal data class GeminiGenerationConfigDto(
    @SerializedName("responseMimeType") val responseMimeType: String,
)

internal data class GeminiResponseDto(
    @SerializedName("candidates") val candidates: List<GeminiCandidateDto>? = null,
)

internal data class GeminiCandidateDto(
    @SerializedName("content") val content: GeminiContentDto? = null,
)
