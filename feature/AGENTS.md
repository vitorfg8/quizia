# Feature Layer — Agent Guidance

Each feature module (`feature:welcome`, `feature:llmsetup`, `feature:home`, `feature:quiz`, `feature:results`, `feature:settings`) follows the same structure:

```
feature:<name>/
 └── src/main/
      ├── java/com/vitorfg8/quizia/feature/<name>/
      │    ├── <Name>Screen.kt        — root composable + previews
      │    ├── <Name>ViewModel.kt     — MVI ViewModel
      │    ├── <Name>UiState.kt       — immutable state data class
      │    ├── <Name>SideEffect.kt    — sealed interface of one-shot effects
      │    └── components/            — smaller composables used only in this feature
      └── res/
           └── values/strings.xml     — feature-scoped strings
```

---

## Navigation Flow

```
AppStart ──► firstRun? ──yes──► WelcomeScreen ──► LlmSetupScreen ──► HomeScreen
                │                                                          ▲
                no ───────────────────────────────────────────────────────┘
                                                                           │
              HomeScreen ──► QuizScreen ──► ResultsScreen ──► HomeScreen  │
              HomeScreen ──► SettingsScreen ──────────────────────────────┘
```

- `isFirstRun` is stored in DataStore; checked in `:app`'s nav graph entry point
- After completing `LlmSetupScreen`, `isFirstRun` is set to `false`
- If an LLM is already configured, `WelcomeScreen` and `LlmSetupScreen` are skipped entirely

---

## Screen Specifications

### WelcomeScreen

- Shown only on first launch
- Full-screen layout; app logo, app name, short tagline
- Single primary button: **"Continue"** → navigates to `LlmSetupScreen`
- No ViewModel needed — stateless screen

**Previews required:** light + dark

---

### LlmSetupScreen

**State:**
```kotlin
data class LlmSetupUiState(
    val availableProviders: List<LlmProviderType> = emptyList(),
    val selectedProvider: LlmProviderType? = null,
    val canContinue: Boolean = false,
)
```

**Behavior:**
- Lists available LLM options as radio buttons
- `LlmProviderType.GEMINI_NANO` is included only when `isGeminiNanoSupported()` returns `true`
- **"Continue"** button is enabled only when `selectedProvider != null`
- On continue: saves selection to DataStore, emits `NavigateToHome` side effect

**Providers displayed:**
1. Gemini Nano *(only if device supports it)*
2. Gemini API
3. OpenAI API
4. Claude API

**Previews required:** light + dark, with at least one option pre-selected

---

### HomeScreen

**State:**
```kotlin
data class HomeUiState(
    val categories: List<QuizCategory> = QuizCategory.entries,
)
```

**Behavior:**
- Displays 6 category cards in a grid or vertical list
- Tapping a category emits `NavigateToQuiz(category)` side effect
- Settings icon in the top bar emits `NavigateToSettings` side effect

**Categories (enum):**
```kotlin
enum class QuizCategory {
    GENERAL_KNOWLEDGE,
    HISTORY_AND_GEOGRAPHY,
    INTERNATIONAL_MUSIC,
    MOVIES_AND_TV,
    SPORTS,
    ASTRONOMY,
    NATURE,
    TECHNOLOGY,
    GAMES,
    CURRENT_EVENTS,
}
```

**Previews required:** light + dark

---

### QuizScreen

**State:**
```kotlin
data class QuizUiState(
    val isLoading: Boolean = false,
    val currentQuestionIndex: Int = 0,
    val totalQuestions: Int = 5,
    val question: Question? = null,
    val selectedOptionIndex: Int? = null,
    val answerRevealed: Boolean = false,
    val correctAnswerText: String = "",
    val errorMessage: String? = null,
)
```

**Side effects:**
```kotlin
sealed interface QuizSideEffect {
    data class NavigateToResults(
        val score: Int,
        val total: Int,
        val elapsedMs: Long,
    ) : QuizSideEffect
}
```

**Behavior:**
- Back button, progress bar and `current / total` counter at the top
- Category chip under the header
- Question text left-aligned
- 4 option cards with a radio on the left:
  - Default: surface + hairline outline, empty radio
  - Selected correct: pale green, filled check radio, trailing check
  - Selected wrong: pale red, selected radio, trailing close
  - Other options stay default after reveal
- Feedback banner after a tap: explanation from the LLM, or a fallback naming the correct option
- **"Next"** / **"See results"** shown only after an option is selected
- Elapsed time is recorded from when questions load and sent with the score

**Previews required:** light + dark, loading, default, correct reveal, wrong reveal, error

---

### ResultsScreen

**State:**
```kotlin
data class ResultsUiState(
    val score: Int = 0,
    val total: Int = 0,
    val elapsedMs: Long = 0L,
)
```

**Behavior:**
- Back arrow returns to Home (same action as **Back to home**)
- Green score ring when `score * 2 >= total`, red otherwise; centre shows `score/total` with no icon
- Konfetti rains from the top when `score` is 80% or more of `total`
- Subtitle: "You got X of Y questions right!"
- Stats card: correct, wrong, elapsed time, separated by hairline dividers
- **"Play another"** starts a new quiz in the same category
- **"Back to home"** returns to `HomeScreen`

**Previews required:** light + dark, success and failure

---

### SettingsScreen

**State:**
```kotlin
data class SettingsUiState(
    val selectedProvider: LlmProviderType = LlmProviderType.GEMINI_API,
    val apiKeyMasked: String = "",       // shows "••••••••1234" or empty
    val theme: AppTheme = AppTheme.SYSTEM,
    val questionCount: Int = 5,
    val availableProviders: List<LlmProviderType> = emptyList(),
    val isSaving: Boolean = false,
)
```

**Behavior:**
- LLM provider picker (radio group) — same rules as `LlmSetupScreen`
- API key text field:
  - Shows masked value when a key is already saved
  - Clear/edit button to enter a new key
  - Key is saved via `ApiKeyStore` (EncryptedSharedPreferences) — never stored in plain text
  - Not shown for `GEMINI_NANO`
- Theme selector: **Light**, **Dark**, **System** (segmented button or radio group)
- Question count selector: **5**, **10**, **15** (segmented button or radio group)
- Changes are saved immediately on selection (no explicit save button needed, except for API key)

**Previews required:** light + dark

---

## Composable Rules for Feature Modules

1. Screen composables must accept `UiState` and event lambdas as parameters — never a `ViewModel` reference
2. Every composable file with a meaningful UI must include `@Preview` functions (light + dark)
3. All colors via `QuiziaTheme.colorScheme.*`; all typography via `QuiziaTheme.typography.*`
4. All text via `stringResource(R.string.…)` — no hardcoded strings
5. No business logic inside composables — delegate to ViewModel via event callbacks
6. Prefer `LaunchedEffect(Unit)` for side-effect collection with `sideEffect.collect { … }`
