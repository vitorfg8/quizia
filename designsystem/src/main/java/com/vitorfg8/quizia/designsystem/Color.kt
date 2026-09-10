package com.vitorfg8.quizia.designsystem

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ── Brand palette ────────────────────────────────────────────────────────────

private val Lilac600 = Color(0xFF6B5CFF)
private val Lilac100 = Color(0xFFE8E6FF)
private val Lilac900 = Color(0xFF2A2270)
private val Lilac400 = Color(0xFF9B8FFF)
private val Lilac200 = Color(0xFFD4D0FF)
private val Lilac700 = Color(0xFF4A3FD4)

private val CanvasLight = Color(0xFFF6F7FB)
private val CanvasDark = Color(0xFF12121A)
private val SurfaceDark = Color(0xFF1C1C26)
private val Ink900 = Color(0xFF111827)
private val Ink700 = Color(0xFF374151)
private val Ink500 = Color(0xFF6B7280)
private val Ink400 = Color(0xFF9CA3AF)
private val LineLight = Color(0xFFE2E4EA)
private val WashLight = Color(0xFFEEF0F6)

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
    onBackground = CanvasLight,
    surface = SurfaceDark,
    onSurface = CanvasLight,
    surfaceVariant = Ink700,
    onSurfaceVariant = Ink400,
    surfaceContainerLow = SurfaceDark,
    outline = Ink500,
    outlineVariant = Ink700,
    error = Red400,
    onError = Red900,
)

// ── Extended colors (correct / wrong answer states) ───────────────────────────

private val Green600 = Color(0xFF16A34A)
private val Green400 = Color(0xFF4ADE80)
private val Green900 = Color(0xFF14532D)

@Immutable
data class QuiziaExtendedColors(
    val correctAnswer: Color,
    val onCorrectAnswer: Color,
    val wrongAnswer: Color,
    val onWrongAnswer: Color,
)

internal val LightExtendedColors = QuiziaExtendedColors(
    correctAnswer = Green600,
    onCorrectAnswer = Color.White,
    wrongAnswer = Red600,
    onWrongAnswer = Color.White,
)

internal val DarkExtendedColors = QuiziaExtendedColors(
    correctAnswer = Green400,
    onCorrectAnswer = Green900,
    wrongAnswer = Red400,
    onWrongAnswer = Red900,
)

internal val LocalQuiziaExtendedColors = staticCompositionLocalOf { LightExtendedColors }
