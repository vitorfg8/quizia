package com.vitorfg8.quizia.designsystem

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ── Brand palette ────────────────────────────────────────────────────────────

private val Blue600 = Color(0xFF1A56DB)
private val Blue100 = Color(0xFFDBEAFE)
private val Blue900 = Color(0xFF1E3A8A)
private val Blue400 = Color(0xFF60A5FA)
private val Blue200 = Color(0xFFBFDBFE)
private val Blue700 = Color(0xFF1D4ED8)

private val Violet600 = Color(0xFF7C3AED)
private val Violet100 = Color(0xFFEDE9FE)
private val Violet900 = Color(0xFF4C1D95)
private val Violet400 = Color(0xFFA78BFA)
private val Violet700 = Color(0xFF5B21B6)

private val NeutralWhite = Color(0xFFF9FAFB)
private val Neutral900 = Color(0xFF111827)
private val Neutral800 = Color(0xFF1F2937)
private val Neutral700 = Color(0xFF374151)
private val Neutral500 = Color(0xFF6B7280)
private val Neutral400 = Color(0xFF9CA3AF)
private val Neutral300 = Color(0xFFD1D5DB)
private val Neutral200 = Color(0xFFF3F4F6)

private val Red600 = Color(0xFFDC2626)
private val Red400 = Color(0xFFF87171)
private val Red900 = Color(0xFF7F1D1D)

// ── Material3 color schemes ───────────────────────────────────────────────────

internal val LightColorScheme = lightColorScheme(
    primary = Blue600,
    onPrimary = Color.White,
    primaryContainer = Blue100,
    onPrimaryContainer = Blue900,
    secondary = Violet600,
    onSecondary = Color.White,
    secondaryContainer = Violet100,
    onSecondaryContainer = Violet900,
    background = NeutralWhite,
    onBackground = Neutral900,
    surface = Color.White,
    onSurface = Neutral900,
    surfaceVariant = Neutral200,
    onSurfaceVariant = Neutral500,
    outline = Neutral300,
    error = Red600,
    onError = Color.White,
)

internal val DarkColorScheme = darkColorScheme(
    primary = Blue400,
    onPrimary = Blue900,
    primaryContainer = Blue700,
    onPrimaryContainer = Blue200,
    secondary = Violet400,
    onSecondary = Violet900,
    secondaryContainer = Violet700,
    onSecondaryContainer = Violet100,
    background = Neutral900,
    onBackground = NeutralWhite,
    surface = Neutral800,
    onSurface = NeutralWhite,
    surfaceVariant = Neutral700,
    onSurfaceVariant = Neutral400,
    outline = Neutral500,
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
