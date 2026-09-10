package com.vitorfg8.quizia.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun QuiziaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(
        LocalQuiziaSpacing provides QuiziaSpacing(),
        LocalQuiziaSizes provides QuiziaSizes(),
        LocalQuiziaExtendedColors provides extendedColors,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = QuiziaTypography,
            shapes = QuiziaShapes,
            content = content,
        )
    }
}

/**
 * Accessor object for Quizia design tokens.
 * Always use this instead of [MaterialTheme] inside feature modules.
 */
object QuiziaTheme {
    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    val spacing: QuiziaSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalQuiziaSpacing.current

    val sizes: QuiziaSizes
        @Composable
        @ReadOnlyComposable
        get() = LocalQuiziaSizes.current

    val extendedColors: QuiziaExtendedColors
        @Composable
        @ReadOnlyComposable
        get() = LocalQuiziaExtendedColors.current
}
