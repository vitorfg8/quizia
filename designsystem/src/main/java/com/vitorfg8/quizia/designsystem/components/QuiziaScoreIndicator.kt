package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

private const val MIN_PROGRESS = 0f
private const val MAX_PROGRESS = 1f
private const val SCORE_RING_ANIMATION_MS = 700

/** Circular score ring with the fraction in the centre. */
@Composable
fun QuiziaScoreIndicator(
    progress: Float,
    label: String,
    isSuccess: Boolean,
    modifier: Modifier = Modifier,
) {
    val accent: Color = if (isSuccess) {
        QuiziaTheme.extendedColors.success
    } else {
        QuiziaTheme.extendedColors.error
    }
    val track: Color = if (isSuccess) {
        QuiziaTheme.extendedColors.successContainer
    } else {
        QuiziaTheme.extendedColors.errorContainer
    }
    val animatedProgress: Float by animateFloatAsState(
        targetValue = progress.coerceIn(MIN_PROGRESS, MAX_PROGRESS),
        animationSpec = tween(durationMillis = SCORE_RING_ANIMATION_MS),
        label = "scoreRingProgress",
    )
    Box(
        modifier = modifier.size(QuiziaTheme.sizes.scoreRingSize),
        contentAlignment = Alignment.Center,
    ) {
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
