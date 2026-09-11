package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

private const val MIN_PROGRESS = 0f
private const val MAX_PROGRESS = 1f
private const val SCORE_RING_ANIMATION_MS = 700
private const val DASH_LENGTH_SCALE = 4f
private const val DASH_HALF_SCALE = 2f
private const val DASH_THICKNESS_SCALE = 4f

private enum class SparkKind {
    Diamond,
    Dash,
    Square,
}

private data class ScoreSpark(
    val x: Float,
    val y: Float,
    val size: Float,
    val rotationDegrees: Float,
    val alpha: Float,
    val kind: SparkKind,
    val usesAccent: Boolean,
)

private val SCORE_SPARKS = listOf(
    ScoreSpark(
        x = 0.16f,
        y = 0.18f,
        size = 0.045f,
        rotationDegrees = 45f,
        alpha = 0.55f,
        kind = SparkKind.Diamond,
        usesAccent = true,
    ),
    ScoreSpark(
        x = 0.84f,
        y = 0.16f,
        size = 0.028f,
        rotationDegrees = 20f,
        alpha = 0.40f,
        kind = SparkKind.Square,
        usesAccent = false,
    ),
    ScoreSpark(
        x = 0.90f,
        y = 0.42f,
        size = 0.050f,
        rotationDegrees = 70f,
        alpha = 0.35f,
        kind = SparkKind.Dash,
        usesAccent = true,
    ),
    ScoreSpark(
        x = 0.12f,
        y = 0.48f,
        size = 0.022f,
        rotationDegrees = 0f,
        alpha = 0.45f,
        kind = SparkKind.Square,
        usesAccent = true,
    ),
    ScoreSpark(
        x = 0.82f,
        y = 0.78f,
        size = 0.038f,
        rotationDegrees = 45f,
        alpha = 0.40f,
        kind = SparkKind.Diamond,
        usesAccent = false,
    ),
    ScoreSpark(
        x = 0.22f,
        y = 0.82f,
        size = 0.032f,
        rotationDegrees = -25f,
        alpha = 0.30f,
        kind = SparkKind.Dash,
        usesAccent = true,
    ),
    ScoreSpark(
        x = 0.50f,
        y = 0.06f,
        size = 0.018f,
        rotationDegrees = 45f,
        alpha = 0.28f,
        kind = SparkKind.Diamond,
        usesAccent = false,
    ),
    ScoreSpark(
        x = 0.08f,
        y = 0.32f,
        size = 0.020f,
        rotationDegrees = 15f,
        alpha = 0.32f,
        kind = SparkKind.Square,
        usesAccent = false,
    ),
)

/** Circular score ring with the fraction in the centre and a few quiet sparks around it. */
@Composable
fun QuiziaScoreIndicator(
    progress: Float,
    label: String,
    isSuccess: Boolean,
    modifier: Modifier = Modifier,
) {
    val accent = if (isSuccess) {
        QuiziaTheme.extendedColors.success
    } else {
        QuiziaTheme.extendedColors.error
    }
    val track = if (isSuccess) {
        QuiziaTheme.extendedColors.successContainer
    } else {
        QuiziaTheme.extendedColors.errorContainer
    }
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(MIN_PROGRESS, MAX_PROGRESS),
        animationSpec = tween(durationMillis = SCORE_RING_ANIMATION_MS),
        label = "scoreRingProgress",
    )
    Box(
        modifier = modifier.size(QuiziaTheme.sizes.scoreRingHalo),
        contentAlignment = Alignment.Center,
    ) {
        ScoreSparks(accent = accent, lilac = QuiziaTheme.colorScheme.primary)
        ScoreRing(progress = animatedProgress, accent = accent, track = track)
        Text(
            text = label,
            style = QuiziaTheme.typography.headlineMedium,
            color = QuiziaTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun ScoreRing(
    progress: Float,
    accent: Color,
    track: Color,
) {
    CircularProgressIndicator(
        progress = { progress },
        modifier = Modifier.size(QuiziaTheme.sizes.scoreRingSize),
        color = accent,
        strokeWidth = QuiziaTheme.sizes.scoreRingStroke,
        trackColor = track,
        strokeCap = StrokeCap.Round,
        gapSize = QuiziaTheme.sizes.elevationNone,
    )
}

@Composable
private fun ScoreSparks(
    accent: Color,
    lilac: Color,
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        SCORE_SPARKS.forEach { spark ->
            val color = if (spark.usesAccent) accent else lilac
            drawScoreSpark(spark = spark, color = color.copy(alpha = spark.alpha))
        }
    }
}

private fun DrawScope.drawScoreSpark(spark: ScoreSpark, color: Color) {
    val center = Offset(x = spark.x * size.width, y = spark.y * size.height)
    val radius = spark.size * size.minDimension
    when (spark.kind) {
        SparkKind.Diamond -> drawDiamond(center = center, radius = radius, color = color)
        SparkKind.Dash -> drawDash(
            center = center,
            radius = radius,
            rotationDegrees = spark.rotationDegrees,
            color = color,
        )
        SparkKind.Square -> drawSquare(
            center = center,
            radius = radius,
            rotationDegrees = spark.rotationDegrees,
            color = color,
        )
    }
}

private fun DrawScope.drawDiamond(center: Offset, radius: Float, color: Color) {
    val path = Path().apply {
        moveTo(center.x, center.y - radius)
        lineTo(center.x + radius, center.y)
        lineTo(center.x, center.y + radius)
        lineTo(center.x - radius, center.y)
        close()
    }
    drawPath(path = path, color = color)
}

private fun DrawScope.drawDash(
    center: Offset,
    radius: Float,
    rotationDegrees: Float,
    color: Color,
) {
    rotate(degrees = rotationDegrees, pivot = center) {
        drawRoundRect(
            color = color,
            topLeft = Offset(
                x = center.x - radius * DASH_HALF_SCALE,
                y = center.y - radius / DASH_THICKNESS_SCALE,
            ),
            size = Size(
                width = radius * DASH_LENGTH_SCALE,
                height = radius / DASH_HALF_SCALE,
            ),
            cornerRadius = CornerRadius(x = radius / DASH_HALF_SCALE, y = radius / DASH_HALF_SCALE),
        )
    }
}

private fun DrawScope.drawSquare(
    center: Offset,
    radius: Float,
    rotationDegrees: Float,
    color: Color,
) {
    rotate(degrees = rotationDegrees, pivot = center) {
        drawRect(
            color = color,
            topLeft = Offset(x = center.x - radius, y = center.y - radius),
            size = Size(width = radius * DASH_HALF_SCALE, height = radius * DASH_HALF_SCALE),
        )
    }
}

@Preview(name = "Success – Light", showBackground = true)
@Preview(name = "Success – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaScoreIndicatorSuccessPreview() {
    QuiziaTheme {
        QuiziaScoreIndicator(
            progress = 0.8f,
            label = "8/10",
            isSuccess = true,
            modifier = Modifier.padding(QuiziaTheme.spacing.medium),
        )
    }
}

@Preview(name = "Failure – Light", showBackground = true)
@Preview(name = "Failure – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaScoreIndicatorFailurePreview() {
    QuiziaTheme {
        QuiziaScoreIndicator(
            progress = 0.4f,
            label = "4/10",
            isSuccess = false,
            modifier = Modifier.padding(QuiziaTheme.spacing.medium),
        )
    }
}
