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
| Confetti | Konfetti Compose 2.0.5 |
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

## Versioning & Commits

### Semantic Versioning

The app version follows [SemVer 2.0.0](https://semver.org): `MAJOR.MINOR.PATCH`.

- `versionName` in `app/build.gradle.kts` is the full SemVer string — never a partial version like `1.0`
- **MAJOR** — an incompatible change to stored data (DataStore keys, `ApiKeyStore` entries) or the removal of a user-facing feature
- **MINOR** — a new backwards-compatible capability: a new screen, LLM provider, or quiz category
- **PATCH** — bug fixes and internal changes with no user-facing feature change
- While the app is pre-`1.0.0` it is unstable, so breaking changes bump **MINOR** instead of MAJOR
- `versionCode` is a monotonically increasing integer, bumped on every release; it is never reused or decreased

### Conventional Commits

Every commit message follows [Conventional Commits 1.0.0](https://www.conventionalcommits.org):

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

**Types**

| Type | Use for | SemVer effect |
|---|---|---|
| `feat` | A new user-facing capability | MINOR |
| `fix` | A bug fix | PATCH |
| `perf` | A performance improvement | PATCH |
| `refactor` | Restructuring with no behaviour change | PATCH |
| `test` | Adding or fixing tests only | none |
| `docs` | Documentation only, including `AGENTS.md` files | none |
| `build` | Gradle scripts, version catalog, dependencies, toolchain | none |
| `ci` | CI pipeline configuration | none |
| `style` | Formatting only, no code change | none |
| `chore` | Housekeeping that fits nothing above | none |

**Scopes** — the module the change belongs to, dropping the leading `:` and the `feature:` prefix:

`app`, `designsystem`, `domain`, `data`, `llm`, `welcome`, `llmsetup`, `home`, `quiz`, `results`, `settings`

Omit the scope when the change spans the whole repository, such as a version catalog bump.

**Rules**

- Description in English, imperative mood, lowercase, no trailing period: `add api key field`, not `Added API key field.`
- Subject line of 72 characters or fewer
- The body explains **why** the change was made, not what the diff already shows; wrap it at 72 columns
- A breaking change is marked with `!` after the scope **and** a `BREAKING CHANGE:` footer describing the migration
- One logical change per commit — never mix a feature with an unrelated refactor
- Tests written alongside a feature belong in that feature's commit; a standalone `test:` commit is only for tests added to pre-existing code
- Split large work into a sequence of commits ordered by dependency: build setup, then `:core:domain`, `:core:data`, `:designsystem`, the features, and finally `:app`

**Examples**

```
feat(llmsetup): add llm selection with byok api key entry
fix(data): fall back to defaults when the datastore read fails
build: enforce detekt and 80% kover branch coverage
docs(designsystem): document the 4.dp dimension grid

feat(data)!: rename the selected provider preference key

BREAKING CHANGE: stored provider selections are dropped and users fall
back to the default provider on first launch after upgrading.
```

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
