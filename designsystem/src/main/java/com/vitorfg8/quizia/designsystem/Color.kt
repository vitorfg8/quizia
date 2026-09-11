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

private val Green600 = Color(0xFF16A34A)
private val Green400 = Color(0xFF4ADE80)
private val Green900 = Color(0xFF14532D)

private val Indigo600 = Color(0xFF4B6BF5)
private val Indigo300 = Color(0xFF7C93FF)
private val Orange600 = Color(0xFFF0703C)
private val Orange300 = Color(0xFFFF9463)
private val Leaf600 = Color(0xFF2FA36B)
private val Leaf300 = Color(0xFF5CC894)

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
    val correctAnswer: Color,
    val onCorrectAnswer: Color,
    val wrongAnswer: Color,
    val onWrongAnswer: Color,
    val brandGradient: List<Color>,
    val onBrandGradient: Color,
    val categoryViolet: Color,
    val categoryIndigo: Color,
    val categoryOrange: Color,
    val categoryGreen: Color,
    val ambientBase: List<Color>,
    val ambientGlow: Color,
)

internal val LightExtendedColors = QuiziaExtendedColors(
    correctAnswer = Green600,
    onCorrectAnswer = Color.White,
    wrongAnswer = Red600,
    onWrongAnswer = Color.White,
    brandGradient = listOf(Lilac500, Lilac700),
    onBrandGradient = Color.White,
    categoryViolet = Lilac600,
    categoryIndigo = Indigo600,
    categoryOrange = Orange600,
    categoryGreen = Leaf600,
    ambientBase = listOf(AmbientLightTop, AmbientLightMid, Color.White),
    ambientGlow = Lilac600,
)

internal val DarkExtendedColors = QuiziaExtendedColors(
    correctAnswer = Green400,
    onCorrectAnswer = Green900,
    wrongAnswer = Red400,
    onWrongAnswer = Red900,
    brandGradient = listOf(Lilac400, Lilac700),
    onBrandGradient = Color.White,
    categoryViolet = Lilac300,
    categoryIndigo = Indigo300,
    categoryOrange = Orange300,
    categoryGreen = Leaf300,
    ambientBase = listOf(AmbientDarkTop, AmbientDarkMid, CanvasDark),
    ambientGlow = Lilac400,
)

internal val LocalQuiziaExtendedColors = staticCompositionLocalOf { LightExtendedColors }
