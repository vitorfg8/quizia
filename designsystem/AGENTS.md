# Design System — Agent Guidance

The `:designsystem` module is the **single source of truth** for all visual tokens and shared UI components. No feature module may define its own colors, typography, spacing, or shapes.

---

## Theme Entry Point

```kotlin
@Composable
fun QuiziaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
)
```

- Wrap every screen composable with `QuiziaTheme` in previews
- In `:app`, `QuiziaTheme` is applied once at the root level driven by the `AppTheme` setting from DataStore
- Feature modules must never call `MaterialTheme { … }` directly

---

## Design Language

- **Style:** minimalist and modern
- **Whitespace:** generous padding; avoid cramped layouts
- **Cards:** rounded corners (`QuiziaShapes.medium`); subtle elevation or outlined style
- **Color:** restrained palette; accent used sparingly for interactive elements
- **Typography:** clean, readable; no decorative fonts

---

## Token Files

| File | Contents |
|---|---|
| `QuiziaColors.kt` | `LightColorScheme` and `DarkColorScheme` (Material3 `ColorScheme`) |
| `QuiziaTypography.kt` | `QuiziaTypography` (`Typography` object with all text styles) |
| `QuiziaShapes.kt` | `QuiziaShapes` (`Shapes` object) |
| `QuiziaSpacing.kt` | `QuiziaSpacing` object with named spacing constants |
| `QuiziaTheme.kt` | `QuiziaTheme` composable + `LocalQuiziaSpacing` `CompositionLocal` |

### QuiziaSpacing example

```kotlin
object QuiziaSpacing {
    val extraSmall = 4.dp
    val small = 8.dp
    val medium = 16.dp
    val large = 24.dp
    val extraLarge = 32.dp
}
```

Access via `QuiziaTheme.spacing.medium` — **never** use literal `dp` values in feature or component code.

---

## Component Catalogue

Every component below lives in `:designsystem` and must be used instead of raw Material3 equivalents.

### QuiziaButton

```kotlin
@Composable
fun QuiziaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
)
```

- Primary action button (filled style)
- Used for "Continue", "Next", "Back to Home"

---

### QuiziaOptionCard

```kotlin
enum class OptionCardState { Default, Correct, Wrong }

@Composable
fun QuiziaOptionCard(
    text: String,
    state: OptionCardState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
)
```

- Background color driven by `state`:
  - `Default` → `QuiziaTheme.colorScheme.surfaceVariant` (gray)
  - `Correct` → green semantic color token
  - `Wrong` → red semantic color token
- `enabled = false` after an answer has been revealed (prevents re-selection)

---

### QuiziaProgressBar

```kotlin
@Composable
fun QuiziaProgressBar(
    current: Int,
    total: Int,
    modifier: Modifier = Modifier,
)
```

- Linear progress indicator: `progress = current.toFloat() / total`
- Displayed at the top of `QuizScreen`

---

### QuiziaStarRating

```kotlin
@Composable
fun QuiziaStarRating(
    stars: Int,           // 0–5
    modifier: Modifier = Modifier,
)
```

- Renders 5 star icons; filled for `stars`, outlined for the rest
- Uses icon tokens from `QuiziaIcons`

---

### CategoryCard

```kotlin
@Composable
fun CategoryCard(
    category: QuizCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
)
```

- Card with category icon and localized label
- Used in `HomeScreen`

---

### LlmProviderRadioItem

```kotlin
@Composable
fun LlmProviderRadioItem(
    provider: LlmProviderType,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
)
```

- Row with radio button + provider name label
- Used in `LlmSetupScreen` and `SettingsScreen`

---

## Usage Rules

1. **No hardcoded colors** — use `QuiziaTheme.colorScheme.*` only
2. **No hardcoded dimensions** — use `QuiziaTheme.spacing.*` or `QuiziaShapes.*` only
3. **No hardcoded strings** — all labels via `stringResource(R.string.…)`
4. **No raw Material3 components** in `:feature:*` — always use the Quizia wrapper components above
5. When a new shared component is needed, add it to `:designsystem` first, then use it in features

---

## Preview Rules

Every component in `:designsystem` must have **both** previews:

```kotlin
@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaButtonPreview() {
    QuiziaTheme {
        QuiziaButton(text = "Continue", onClick = {})
    }
}
```

- Previews use **fake/hardcoded** data — never a real ViewModel or repository
- Previews are always `private`
- Always wrap with `QuiziaTheme { … }`
- Always set `showBackground = true`

---

## Adding New Tokens or Components

1. Define the token in the appropriate token file (`QuiziaColors.kt`, `QuiziaSpacing.kt`, etc.)
2. Expose it through `QuiziaTheme` if it needs to be accessed via `CompositionLocal`
3. Create the component in `:designsystem/src/main/java/com/vitorfg8/quizia/designsystem/components/`
4. Add light + dark previews in the same file
5. Export the component from the module's public API (no `internal` modifier for shared components)
6. **Never** duplicate a component across feature modules — add it to `:designsystem` once
