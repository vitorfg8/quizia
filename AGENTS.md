# Quizia — Agent Guidance

## Project Overview

Quizia is an AI-powered quiz app for Android. Questions are generated on demand by a large language model chosen by the user. The app follows a **Bring Your Own Key (BYOK)** model: users provide their own API keys for cloud providers. On-device inference (Gemini Nano) requires no key when the device supports it.

The app supports **English** and **Brazilian Portuguese** (locale-driven, via string resources).

---

## Module Graph

```
:app
 ├── :designsystem
 ├── :feature:welcome
 ├── :feature:llmsetup
 ├── :feature:home
 ├── :feature:quiz
 ├── :feature:results
 ├── :feature:settings
 └── :core:domain
      └── :core:data
           └── :core:llm
```

Each `:feature:*` module depends on `:designsystem` and `:core:domain`.  
`:core:data` depends on `:core:domain`.  
`:core:llm` depends on `:core:domain`.  
`:app` wires the navigation graph and starts Koin.

---

## Tech Stack

| Layer | Library / Tool |
|---|---|
| Language | Kotlin 2.4.20 |
| UI | Jetpack Compose + Material3 |
| DI | Koin 4.2.2 |
| Navigation | Navigation Compose 2.10.1 |
| HTTP | Retrofit 3.0.0 + OkHttp 5.5.0 + Gson 2.14.0 |
| LLM SDK | Google Generative AI 0.9.0 (Gemini API + Nano) |
| Preferences | DataStore Preferences 1.2.1 |
| Secure storage | EncryptedSharedPreferences (security-crypto 1.1.0-alpha06) |
| Static analysis | Detekt 1.23.8 |
| Coverage | Kover 0.9.9 (≥ 80% branch per module) |
| Unit tests | JUnit 4 + MockK 1.13.12 + kotlinx-coroutines-test |
| Build system | Gradle 9.6 + AGP 9.4.0 + KSP 2.3.12 |

---

## Architecture

### Layering rules

- **`:core:domain`** — pure Kotlin; no Android imports; holds models, repository interfaces, and use-case classes
- **`:core:data`** — implements repository interfaces from domain; owns DataStore and `ApiKeyStore`
- **`:core:llm`** — implements `LlmProvider`; each provider is a separate class; Koin module exposes them with named qualifiers
- **`:feature:*`** — Compose screens + ViewModels; depends only on `:core:domain` (never on `:core:data` or `:core:llm` directly)
- **`:app`** — wires navigation, starts Koin with all modules

### MVI pattern

Every screen must follow this structure:

```kotlin
// State — immutable snapshot of what the UI renders
data class ExampleUiState(
    val isLoading: Boolean = false,
    val items: List<Item> = emptyList(),
    val errorMessage: String? = null,
)

// Side effects — one-shot events that the UI consumes
sealed interface ExampleSideEffect {
    data object NavigateToHome : ExampleSideEffect
    data class ShowError(val message: String) : ExampleSideEffect
}

// ViewModel — single source of truth
class ExampleViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ExampleUiState())
    val uiState: StateFlow<ExampleUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<ExampleSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<ExampleSideEffect> = _sideEffect.receiveAsFlow()
}
```

Screen composables **must not** contain business logic. They observe `uiState` and forward events to the ViewModel.

---

## Naming & Code Style

- PascalCase for classes, sealed interfaces, objects
- camelCase for functions, properties, local variables
- UPPER_SNAKE_CASE for constants (`companion object { const val … }`)
- Function names start with a verb: `loadQuiz`, `selectOption`, `navigateToHome`
- Boolean properties start with `is`, `has`, or `can`: `isLoading`, `hasError`, `canProceed`
- No magic numbers — define named constants
- No abbreviations except standard ones (API, URL, LLM, UI, DB)
- Functions must be short (< 20 lines) and single-purpose
- Avoid deep nesting — prefer early returns and extracted functions

---

## Quality Gates

**All rules below are mandatory. Violations fail the build.**

### Detekt

- Run: `./gradlew detekt`
- All rules in `detekt.yml` must pass
- Suppressions are allowed only with an inline comment explaining why: `@Suppress("…") // reason`
- The `detekt-compose` ruleset is active — composable naming, preview rules, and side-effect rules are enforced

### Kover (coverage)

- Run: `./gradlew koverHtmlReport`
- Minimum **80% branch coverage** per module
- New code must not reduce coverage below the threshold
- ViewModels, use cases, and repository implementations must be unit-tested

### Design system

- Never use hardcoded colors (`Color(0xFF…)`, `Color.Red`, etc.)
- Never use hardcoded dimensions (`16.dp`, `24.sp`, etc.) — use `QuiziaTheme.spacing.*` for gaps and `QuiziaTheme.sizes.*` for component dimensions; literals are allowed only in the `:designsystem` token files, and every value must be a multiple of 4.dp
- Never use hardcoded strings in composables — always use `stringResource(R.string.…)`
- Never call `MaterialTheme.*` inside `:feature:*` — use `QuiziaTheme.*`

### Compose previews

- Every composable that renders meaningful UI must have:
  - `@Preview(name = "Light")` with `showBackground = true`
  - `@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES)` with `showBackground = true`
- Previews must use hardcoded/fake data — never inject real ViewModels or repositories

### Tests

- Use **MockK** for all test doubles: `mockk()`, `every { } returns`, `coEvery { } returns`, `verify { }`
- Use `runTest` with `StandardTestDispatcher` for coroutine tests
- Follow Arrange-Act-Assert structure
- Name variables: `inputX`, `mockX`, `actualX`, `expectedX`

---

## LLM Providers

The active provider is resolved by Koin using the user-selected `LlmProviderType` stored in DataStore.

| Provider | Koin qualifier | Requires API key |
|---|---|---|
| Gemini Nano (on-device) | `named("gemini_nano")` | No — only shown when device supports it |
| Gemini API | `named("gemini_api")` | Yes |
| OpenAI API | `named("openai")` | Yes |
| Claude API | `named("claude")` | Yes |

---

## Localization

- Default locale: English (`values/strings.xml`)
- Supported locale: Brazilian Portuguese (`values-pt-rBR/strings.xml`)
- Both files must be kept in sync — adding a string to one requires adding to the other
- String resources live in each feature module's `res/values/` directory
- Never hardcode user-facing text in composables
