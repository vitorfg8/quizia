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
| `Spacing.kt` | `QuiziaSpacing` — gaps, margins and padding |
| `Size.kt` | `QuiziaSizes` — fixed component dimensions and elevations |
| `Theme.kt` | `QuiziaTheme` composable + every `CompositionLocal` |

### The 4.dp grid

**Every dimension in the design system must be a multiple of 4.dp.** This keeps components
aligned to the same grid regardless of which token they use.

```kotlin
@Immutable
data class QuiziaSpacing(
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 32.dp,
    val huge: Dp = 48.dp,
)

@Immutable
data class QuiziaSizes(
    val iconSmall: Dp = 16.dp,
    val iconMedium: Dp = 24.dp,
    val iconLarge: Dp = 40.dp,
    val iconHuge: Dp = 96.dp,
    val buttonHeight: Dp = 52.dp,
    val minTouchTarget: Dp = 48.dp,
    val progressBarHeight: Dp = 8.dp,
    val elevationNone: Dp = 0.dp,
    val elevationRaised: Dp = 4.dp,
)
```

Use `QuiziaTheme.spacing.*` for the space **between** or **around** elements, and
`QuiziaTheme.sizes.*` for the intrinsic dimensions **of** an element (icon size, component
height, elevation).

A literal `dp` value is only allowed inside `Spacing.kt`, `Size.kt` and `Shape.kt`. Anywhere
else — including `:designsystem` components — it is a violation:

```kotlin
// Wrong
Modifier.size(40.dp)
CardDefaults.cardElevation(defaultElevation = 0.dp)

// Right
Modifier.size(QuiziaTheme.sizes.iconLarge)
CardDefaults.cardElevation(defaultElevation = QuiziaTheme.sizes.elevationNone)
```

When a component needs a dimension that no token covers, add a named token to `QuiziaSizes`
rounded to the nearest multiple of 4.dp — never inline the literal.

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
    leadingIcon: ImageVector? = null,
)
```

- Primary action button: a pill filled with `QuiziaTheme.extendedColors.brandGradient`
- Falls back to a flat `surfaceVariant` fill when `enabled = false`
- Optional `leadingIcon` for actions such as "Play another"
- Used for "Continue", "Next", "Save", "Play another"

---

### QuiziaOutlinedButton

```kotlin
@Composable
fun QuiziaOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
)
```

- Secondary pill: purple outline, transparent fill
- Used for "Back to home" under the primary results CTA

---

### QuiziaWordmark

```kotlin
@Composable
fun QuiziaWordmark(
    modifier: Modifier = Modifier,
    style: TextStyle = QuiziaTheme.typography.titleLarge,
)
```

- Renders the brand name as a dark "Quiz" followed by a purple "ia"
- Always uses Inter ExtraBold, regardless of the size passed in `style`
- Used on the welcome screen and in the home top bar

---

### QuiziaBrandMark

```kotlin
@Composable
fun QuiziaBrandMark(modifier: Modifier = Modifier)
```

- Rounded tile filled with the brand gradient, holding the Quizia sparkle
- Same sparkle as the launcher icon

---

### QuiziaAmbientBackground

```kotlin
@Composable
fun QuiziaAmbientBackground(modifier: Modifier = Modifier)
```

- Decorative welcome backdrop drawn on a `Canvas`: a diagonal wash plus lilac glows and sparkles
- Every coordinate is a fraction of the canvas, so it scales to any screen
- Colors come from `QuiziaTheme.extendedColors.ambientBase` and `ambientGlow`

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

- Background and hairline driven by `state`. Every option has a radio on the left:
  - `Default` → `surface` + outline, empty radio
  - `Correct` → `extendedColors.successContainer`, selected green radio, trailing check
  - `Wrong` → `extendedColors.errorContainer`, selected red radio, trailing close
- `enabled = false` after an answer has been revealed (prevents re-selection)

---

### QuiziaCategoryChip

```kotlin
@Composable
fun QuiziaCategoryChip(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
)
```

- Compact lavender pill with a Material icon and the category name
- Used at the top of `QuizScreen`

---

### QuiziaAnswerFeedback

```kotlin
@Composable
fun QuiziaAnswerFeedback(
    isCorrect: Boolean,
    title: String,
    explanation: String,
    modifier: Modifier = Modifier,
)
```

- Success or error banner shown after the player picks an option
- Title and explanation are passed in so the quiz module owns the copy

---

### QuiziaScoreIndicator

```kotlin
@Composable
fun QuiziaScoreIndicator(
    progress: Float,
    label: String,
    isSuccess: Boolean,
    modifier: Modifier = Modifier,
)
```

- Circular score ring: green when `isSuccess`, red otherwise
- Centre shows only `label` (the fraction, e.g. `"8/10"`) — no star, check or close

---

### QuiziaStatRow

```kotlin
@Composable
fun QuiziaStatRow(
    icon: ImageVector,
    label: String,
    value: String,
    iconTint: Color,
    modifier: Modifier = Modifier,
)
```

- One line of the results stats card: icon and label on the left, value on the right

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

### QuiziaRadioOption

```kotlin
@Composable
fun QuiziaRadioOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
)
```

- Exclusive choice row; the selected one sits on `primaryContainer` so it reads at a glance
- Used for the provider list in `LlmSetupScreen` and `SettingsScreen`, and for the theme picker

---

### QuiziaChoiceChips

```kotlin
@Composable
fun QuiziaChoiceChips(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
)
```

- Row of equally sized pills for a short exclusive set, such as the question count

---

### QuiziaSettingsSection

```kotlin
@Composable
fun QuiziaSettingsSection(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
)
```

- Settings block: purple icon, title, a line of guidance, then the controls
- Used for every section of `SettingsScreen`

---

## Usage Rules

1. **No hardcoded colors** — use `QuiziaTheme.colorScheme.*` only
2. **No hardcoded dimensions** — use `QuiziaTheme.spacing.*`, `QuiziaTheme.sizes.*` or
   `QuiziaTheme.shapes.*` only; all token values are multiples of 4.dp
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
