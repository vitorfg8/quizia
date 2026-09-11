package com.vitorfg8.quizia.designsystem

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ── Brand palette ────────────────────────────────────────────────────────────

private val Lilac700 = Color(0xFF5B49F5)
private val Lilac600 = Color(0xFF6B5CFF)
private val Lilac500 = Color(0xFF7C6CFF)
private val Lilac400 = Color(0xFF8B7BFF)
private val Lilac300 = Color(0xFF9A8BFF)
private val Lilac200 = Color(0xFFD4D0FF)
private val Lilac100 = Color(0xFFEDEAFF)
private val Lilac900 = Color(0xFF1B1440)

private val CanvasLight = Color(0xFFFAF8FF)
private val WashLight = Color(0xFFF1EDFB)
private val LineLight = Color(0xFFE8E3F5)
private val Ink900 = Color(0xFF171526)
private val Ink500 = Color(0xFF6F6A87)

private val CanvasDark = Color(0xFF0D0C16)
private val SurfaceDark = Color(0xFF191827)
private val WashDark = Color(0xFF232135)
private val LineDark = Color(0xFF2F2C45)
private val Paper = Color(0xFFF3F1FB)
private val Ink300 = Color(0xFFA6A1BE)

private val Red600 = Color(0xFFDC2626)
private val Red400 = Color(0xFFF87171)
private val Red900 = Color(0xFF7F1D1D)

// ── Material3 color schemes ───────────────────────────────────────────────────

internal val LightColorScheme = lightColorScheme(
    primary = Lilac600,
    onPrimary = Color.White,
    primaryContainer = Lilac100,
    onPrimaryContainer = Lilac900,
    secondary = Lilac600,
    onSecondary = Color.White,
    secondaryContainer = Lilac100,
    onSecondaryContainer = Lilac900,
    background = CanvasLight,
    onBackground = Ink900,
    surface = Color.White,
    onSurface = Ink900,
    surfaceVariant = WashLight,
    onSurfaceVariant = Ink500,
    surfaceContainerLow = Color.White,
    outline = LineLight,
    outlineVariant = LineLight,
    error = Red600,
    onError = Color.White,
)

internal val DarkColorScheme = darkColorScheme(
    primary = Lilac400,
    onPrimary = Lilac900,
    primaryContainer = Lilac700,
    onPrimaryContainer = Lilac200,
    secondary = Lilac400,
    onSecondary = Lilac900,
    secondaryContainer = Lilac700,
    onSecondaryContainer = Lilac100,
    background = CanvasDark,
    onBackground = Paper,
    surface = SurfaceDark,
    onSurface = Paper,
    surfaceVariant = WashDark,
    onSurfaceVariant = Ink300,
    surfaceContainerLow = SurfaceDark,
    outline = LineDark,
    outlineVariant = LineDark,
    error = Red400,
    onError = Red900,
)

// ── Extended colors ───────────────────────────────────────────────────────────

private val Green700 = Color(0xFF166534)
private val Green600 = Color(0xFF16A34A)
private val Green400 = Color(0xFF4ADE80)
private val Green900 = Color(0xFF14532D)
private val SuccessWashLight = Color(0xFFECFDF3)
private val SuccessWashDark = Color(0xFF14241A)

private val Red800 = Color(0xFF991B1B)
private val ErrorWashLight = Color(0xFFFEF2F2)
private val ErrorWashDark = Color(0xFF2A1416)

private val Indigo600 = Color(0xFF4B6BF5)
private val Indigo300 = Color(0xFF7C93FF)
private val Orange600 = Color(0xFFF0703C)
private val Orange300 = Color(0xFFFF9463)
private val Leaf600 = Color(0xFF2FA36B)
private val Leaf300 = Color(0xFF5CC894)
private val Rose600 = Color(0xFFE25D8C)
private val Rose300 = Color(0xFFF08BB0)
private val Teal600 = Color(0xFF2B9EAF)
private val Teal300 = Color(0xFF5EC9D4)
private val Amber600 = Color(0xFFD4943C)
private val Amber300 = Color(0xFFE8B86A)
private val Orchid600 = Color(0xFFB85FD4)
private val Orchid300 = Color(0xFFD494EA)

private val AmbientLightTop = Color(0xFFF0EAFF)
private val AmbientLightMid = Color(0xFFFAF8FE)
private val AmbientDarkTop = Color(0xFF161326)
private val AmbientDarkMid = Color(0xFF100F1C)

/**
 * Colors the Material 3 scheme has no slot for: answer feedback, the accents that
 * separate the category cards, and the decorative welcome backdrop.
 */
@Immutable
data class QuiziaExtendedColors(
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val brandGradient: List<Color>,
    val onBrandGradient: Color,
    val categoryViolet: Color,
    val categoryIndigo: Color,
    val categoryOrange: Color,
    val categoryGreen: Color,
    val categoryRose: Color,
    val categoryTeal: Color,
    val categoryAmber: Color,
    val categoryOrchid: Color,
    val ambientBase: List<Color>,
    val ambientGlow: Color,
)

internal val LightExtendedColors = QuiziaExtendedColors(
    success = Green600,
    onSuccess = Color.White,
    successContainer = SuccessWashLight,
    onSuccessContainer = Green700,
    error = Red600,
    onError = Color.White,
    errorContainer = ErrorWashLight,
    onErrorContainer = Red800,
    brandGradient = listOf(Lilac500, Lilac700),
    onBrandGradient = Color.White,
    categoryViolet = Lilac600,
    categoryIndigo = Indigo600,
    categoryOrange = Orange600,
    categoryGreen = Leaf600,
    categoryRose = Rose600,
    categoryTeal = Teal600,
    categoryAmber = Amber600,
    categoryOrchid = Orchid600,
    ambientBase = listOf(AmbientLightTop, AmbientLightMid, Color.White),
    ambientGlow = Lilac600,
)

internal val DarkExtendedColors = QuiziaExtendedColors(
    success = Green400,
    onSuccess = Green900,
    successContainer = SuccessWashDark,
    onSuccessContainer = Green400,
    error = Red400,
    onError = Red900,
    errorContainer = ErrorWashDark,
    onErrorContainer = Red400,
    brandGradient = listOf(Lilac400, Lilac700),
    onBrandGradient = Color.White,
    categoryViolet = Lilac300,
    categoryIndigo = Indigo300,
    categoryOrange = Orange300,
    categoryGreen = Leaf300,
    categoryRose = Rose300,
    categoryTeal = Teal300,
    categoryAmber = Amber300,
    categoryOrchid = Orchid300,
    ambientBase = listOf(AmbientDarkTop, AmbientDarkMid, CanvasDark),
    ambientGlow = Lilac400,
)

internal val LocalQuiziaExtendedColors = staticCompositionLocalOf { LightExtendedColors }
