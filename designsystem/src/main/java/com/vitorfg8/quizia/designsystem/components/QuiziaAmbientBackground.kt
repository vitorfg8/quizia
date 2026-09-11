package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vitorfg8.quizia.designsystem.QuiziaTheme

/** A soft lilac glow. Coordinates and radius are fractions of the canvas so it scales with any screen. */
private data class AmbientGlow(
    val centerX: Float,
    val centerY: Float,
    val radius: Float,
    val alpha: Float,
)

/** A four-pointed sparkle. Same fractional coordinate system as [AmbientGlow]. */
private data class AmbientSparkle(
    val centerX: Float,
    val centerY: Float,
    val radius: Float,
    val alpha: Float,
)

private val AMBIENT_GLOWS = listOf(
    AmbientGlow(centerX = -0.12f, centerY = 0.08f, radius = 0.80f, alpha = 0.30f),
    AmbientGlow(centerX = 1.05f, centerY = 0.18f, radius = 0.62f, alpha = 0.20f),
    AmbientGlow(centerX = 0.50f, centerY = 0.38f, radius = 0.45f, alpha = 0.10f),
    AmbientGlow(centerX = 0.10f, centerY = 0.66f, radius = 0.70f, alpha = 0.16f),
)

private val AMBIENT_SPARKLES = listOf(
    AmbientSparkle(centerX = 0.82f, centerY = 0.15f, radius = 0.045f, alpha = 0.28f),
    AmbientSparkle(centerX = 0.16f, centerY = 0.27f, radius = 0.028f, alpha = 0.20f),
    AmbientSparkle(centerX = 0.88f, centerY = 0.57f, radius = 0.022f, alpha = 0.16f),
    AmbientSparkle(centerX = 0.24f, centerY = 0.75f, radius = 0.035f, alpha = 0.18f),
)

private const val GLOW_MID_STOP = 0.55f
private const val GLOW_MID_ALPHA_FRACTION = 0.45f
private const val SPARKLE_WAIST_FRACTION = 0.22f

/**
 * Decorative backdrop for the welcome screen: a diagonal wash, a few out-of-focus lilac
 * glows and scattered sparkles. Drawn entirely in Compose so it stays crisp at any density.
 */
@Composable
fun QuiziaAmbientBackground(modifier: Modifier = Modifier) {
    val baseColors = QuiziaTheme.extendedColors.ambientBase
    val glowColor = QuiziaTheme.extendedColors.ambientGlow
    Canvas(modifier = modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.linearGradient(
                colors = baseColors,
                start = Offset.Zero,
                end = Offset(size.width, size.height),
            ),
        )
        AMBIENT_GLOWS.forEach { drawAmbientGlow(glow = it, color = glowColor) }
        AMBIENT_SPARKLES.forEach { drawAmbientSparkle(sparkle = it, color = glowColor) }
    }
}

private fun DrawScope.drawAmbientGlow(glow: AmbientGlow, color: Color) {
    val center = Offset(x = glow.centerX * size.width, y = glow.centerY * size.height)
    val radius = glow.radius * size.width
    drawCircle(
        brush = Brush.radialGradient(
            colorStops = arrayOf(
                0f to color.copy(alpha = glow.alpha),
                GLOW_MID_STOP to color.copy(alpha = glow.alpha * GLOW_MID_ALPHA_FRACTION),
                1f to Color.Transparent,
            ),
            center = center,
            radius = radius,
        ),
        radius = radius,
        center = center,
    )
}

private fun DrawScope.drawAmbientSparkle(sparkle: AmbientSparkle, color: Color) {
    val center = Offset(x = sparkle.centerX * size.width, y = sparkle.centerY * size.height)
    drawPath(
        path = buildSparklePath(center = center, radius = sparkle.radius * size.width),
        color = color.copy(alpha = sparkle.alpha),
    )
}

/** Four-pointed star whose sides curve inwards, matching the sparkle in the brand mark. */
private fun buildSparklePath(center: Offset, radius: Float): Path {
    val waist = radius * SPARKLE_WAIST_FRACTION
    return Path().apply {
        moveTo(center.x, center.y - radius)
        quadraticTo(center.x + waist, center.y - waist, center.x + radius, center.y)
        quadraticTo(center.x + waist, center.y + waist, center.x, center.y + radius)
        quadraticTo(center.x - waist, center.y + waist, center.x - radius, center.y)
        quadraticTo(center.x - waist, center.y - waist, center.x, center.y - radius)
        close()
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaAmbientBackgroundPreview() {
    QuiziaTheme {
        Box(modifier = Modifier.size(width = 320.dp, height = 640.dp)) {
            QuiziaAmbientBackground()
        }
    }
}
