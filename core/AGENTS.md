# Core Layer — Agent Guidance

The core layer is split into three modules:

| Module | Responsibility |
|---|---|
| `:core:domain` | Pure Kotlin models, repository interfaces, use-case classes |
| `:core:data` | Repository implementations, DataStore, ApiKeyStore |
| `:core:llm` | LlmProvider implementations + Koin bindings |

**Dependency direction:** `:core:llm` → `:core:domain` ← `:core:data`

No module in `:core:*` may depend on a `:feature:*` module.  
`:core:domain` must have **zero Android imports** — it is pure Kotlin.

---

## Domain Models (`:core:domain`)

```kotlin
enum class LlmProviderType {
    GEMINI_NANO,
    GEMINI_API,
    OPENAI,
    CLAUDE,
}

enum class QuizCategory {
    GENERAL_KNOWLEDGE,
    HISTORY_AND_GEOGRAPHY,
    INTERNATIONAL_MUSIC,
    MOVIES_AND_TV,
    SPORTS,
    ASTRONOMY,
}

enum class AppTheme {
    LIGHT,
    DARK,
    SYSTEM,
}

data class QuizRequest(
    val category: QuizCategory,
    val questionCount: Int,       // 5, 10, or 15
    val language: String,         // "en" or "pt"
)

data class QuizResponse(
    val questions: List<Question>,
)

data class Question(
    val text: String,
    val options: List<String>,    // always exactly 4 elements
    val correctIndex: Int,        // 0–3
)

data class AppSettings(
    val selectedProvider: LlmProviderType,
    val theme: AppTheme,
    val questionCount: Int,
    val isFirstRun: Boolean,
)
```

---

## Repository Interfaces (`:core:domain`)

```kotlin
interface SettingsRepository {
    fun observeSettings(): Flow<AppSettings>
    suspend fun saveSelectedProvider(provider: LlmProviderType)
    suspend fun saveTheme(theme: AppTheme)
    suspend fun saveQuestionCount(count: Int)
    suspend fun setFirstRunComplete()
}

interface ApiKeyRepository {
    fun getApiKey(provider: LlmProviderType): String?
    fun saveApiKey(provider: LlmProviderType, key: String)
    fun clearApiKey(provider: LlmProviderType)
}
```

---

## LLM Abstraction (`:core:llm`)

### Interface

```kotlin
interface LlmProvider {
    suspend fun generateQuiz(request: QuizRequest): Result<QuizResponse>
}
```

### Implementations

| Class | Provider | Key required |
|---|---|---|
| `GeminiNanoProvider` | On-device Gemini Nano | No |
| `GeminiApiProvider` | Google Generative AI SDK | Yes |
| `OpenAiProvider` | OpenAI REST API | Yes |
| `ClaudeApiProvider` | Anthropic REST API | Yes |

### Gemini Nano support check

```kotlin
fun isGeminiNanoSupported(context: Context): Boolean
```

This function must be used in `LlmSetupScreen` and `SettingsScreen` to conditionally show the Gemini Nano option.

### Koin module

```kotlin
val llmModule = module {
    single(named("gemini_nano")) { GeminiNanoProvider(get()) }
    single(named("gemini_api")) { GeminiApiProvider(get(), get()) }
    single(named("openai")) { OpenAiProvider(get(), get()) }
    single(named("claude")) { ClaudeApiProvider(get(), get()) }

    // Resolves the active provider from the user's saved preference
    single<LlmProvider> {
        val settings = get<SettingsRepository>().observeSettings()
        // Provider is resolved dynamically at call time — see ActiveLlmProvider
        ActiveLlmProvider(get(), getKoin())
    }
}
```

### Prompt contract

Each `LlmProvider` implementation must request quiz questions using a structured prompt that:
1. Specifies the category and language
2. Requests exactly `questionCount` questions
3. Requests exactly 4 options per question
4. Requests a structured JSON response with fields: `text`, `options` (array of 4), `correctIndex` (0–3)
5. Instructs the model not to include explanations outside the JSON structure

---

## Data Layer (`:core:data`)

### SettingsRepositoryImpl

- Backed by **DataStore Preferences**
- Keys: `selected_provider`, `app_theme`, `question_count`, `is_first_run`
- Implements `SettingsRepository`

### ApiKeyStore (wraps EncryptedSharedPreferences)

```kotlin
class ApiKeyStore(context: Context) {
    fun getKey(provider: LlmProviderType): String?
    fun saveKey(provider: LlmProviderType, key: String)
    fun clearKey(provider: LlmProviderType)
}
```

- Uses `EncryptedSharedPreferences` backed by the Android Keystore
- **Never** exposes raw key strings via logs or non-encrypted storage
- `ApiKeyRepositoryImpl` delegates entirely to `ApiKeyStore`

### Koin module

```kotlin
val dataModule = module {
    single { createDataStore(androidContext()) }
    single { ApiKeyStore(androidContext()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    single<ApiKeyRepository> { ApiKeyRepositoryImpl(get()) }
}
```

---

## Testing Rules for Core Layer

### General

- All unit tests live in `src/test/` (JVM); no Android dependencies in `:core:domain` tests
- Use **MockK** for collaborators: `mockk<SettingsRepository>()`, `coEvery { … } returns …`
- Use `runTest` + `StandardTestDispatcher` for all `suspend` functions and `Flow` tests
- Follow Arrange-Act-Assert structure; name variables `inputX`, `mockX`, `actualX`, `expectedX`

### Repository tests

Cover all three paths for every repository method:
1. **Happy path** — valid data returned/saved correctly
2. **Empty / default** — no data stored yet, defaults are returned
3. **Error** — exception thrown, `Result.Failure` or empty `Flow` emitted

### LlmProvider tests

- Mock the underlying HTTP client or SDK call
- Test that the prompt is constructed correctly for each category and language
- Test JSON parsing of the model response into `QuizResponse`
- Test error propagation when the model returns malformed JSON or throws

### ApiKeyStore tests

- Use an in-memory or robolectric context for `EncryptedSharedPreferences` tests
- Verify that stored keys are retrievable and that `clearKey` removes them
- Never assert on the raw encrypted bytes — only on the decrypted value

---

## Kover Minimum Coverage

`:core:domain` — 90% (pure logic, no Android)  
`:core:data` — 80%  
`:core:llm` — 80%
