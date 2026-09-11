package com.vitorfg8.quizia.core.llm.di

import com.google.gson.Gson
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.core.domain.repository.LlmProvider
import com.vitorfg8.quizia.core.domain.repository.OnDeviceModelRepository
import com.vitorfg8.quizia.core.llm.network.ClaudeService
import com.vitorfg8.quizia.core.llm.network.GeminiApiService
import com.vitorfg8.quizia.core.llm.network.OpenAiService
import com.vitorfg8.quizia.core.llm.ondevice.GeminiNanoAvailability
import com.vitorfg8.quizia.core.llm.ondevice.MlKitOnDeviceModelSession
import com.vitorfg8.quizia.core.llm.ondevice.OnDeviceModelSession
import com.vitorfg8.quizia.core.llm.parser.QuizJsonParser
import com.vitorfg8.quizia.core.llm.prompt.QuizPromptBuilder
import com.vitorfg8.quizia.core.llm.provider.ActiveLlmProvider
import com.vitorfg8.quizia.core.llm.provider.ClaudeProvider
import com.vitorfg8.quizia.core.llm.provider.GeminiApiProvider
import com.vitorfg8.quizia.core.llm.provider.GeminiNanoProvider
import com.vitorfg8.quizia.core.llm.provider.OpenAiProvider
import com.vitorfg8.quizia.core.llm.provider.RemoteQuizGenerator
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Clock
import java.util.concurrent.TimeUnit

private const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/"
private const val OPENAI_BASE_URL = "https://api.openai.com/"
private const val CLAUDE_BASE_URL = "https://api.anthropic.com/"

/** Generating a whole quiz routinely takes tens of seconds, so the default 10s is too short. */
private const val TIMEOUT_SECONDS = 90L

val llmModule = module {
    single { Gson() }
    single { QuizPromptBuilder(clock = Clock.systemDefaultZone()) }
    single { QuizJsonParser(get()) }
    single { RemoteQuizGenerator(get(), get(), get()) }

    single {
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .callTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    single<GeminiApiService> { createService(get(), get(), GEMINI_BASE_URL) }
    single<OpenAiService> { createService(get(), get(), OPENAI_BASE_URL) }
    single<ClaudeService> { createService(get(), get(), CLAUDE_BASE_URL) }

    single<OnDeviceModelSession> { MlKitOnDeviceModelSession() }
    single<OnDeviceModelRepository> { GeminiNanoAvailability(get()) }

    single<LlmProvider>(named("gemini_nano")) { GeminiNanoProvider(get(), get(), get()) }
    single<LlmProvider>(named("gemini_api")) { GeminiApiProvider(get(), get()) }
    single<LlmProvider>(named("openai")) { OpenAiProvider(get(), get()) }
    single<LlmProvider>(named("claude")) { ClaudeProvider(get(), get()) }

    single<LlmProvider> {
        ActiveLlmProvider(
            settingsRepository = get(),
            providers = mapOf(
                LlmProviderType.GEMINI_NANO to get(named("gemini_nano")),
                LlmProviderType.GEMINI_API to get(named("gemini_api")),
                LlmProviderType.OPENAI to get(named("openai")),
                LlmProviderType.CLAUDE to get(named("claude")),
            ),
        )
    }
}

private inline fun <reified T : Any> createService(
    client: OkHttpClient,
    gson: Gson,
    baseUrl: String,
): T = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()
    .create(T::class.java)
