package com.vitorfg8.quizia.core.llm.network

import com.vitorfg8.quizia.core.llm.network.dto.ClaudeRequestDto
import com.vitorfg8.quizia.core.llm.network.dto.ClaudeResponseDto
import com.vitorfg8.quizia.core.llm.network.dto.GeminiRequestDto
import com.vitorfg8.quizia.core.llm.network.dto.GeminiResponseDto
import com.vitorfg8.quizia.core.llm.network.dto.OpenAiRequestDto
import com.vitorfg8.quizia.core.llm.network.dto.OpenAiResponseDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

internal interface GeminiApiService {
    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model") model: String,
        @Header("x-goog-api-key") apiKey: String,
        @Body body: GeminiRequestDto,
    ): GeminiResponseDto
}

internal interface OpenAiService {
    @POST("v1/chat/completions")
    suspend fun createChatCompletion(
        @Header("Authorization") authorization: String,
        @Body body: OpenAiRequestDto,
    ): OpenAiResponseDto
}

internal interface ClaudeService {
    @POST("v1/messages")
    suspend fun createMessage(
        @Header("x-api-key") apiKey: String,
        @Header("anthropic-version") anthropicVersion: String,
        @Body body: ClaudeRequestDto,
    ): ClaudeResponseDto
}
