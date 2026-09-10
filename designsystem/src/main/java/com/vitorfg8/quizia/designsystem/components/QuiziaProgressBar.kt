package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

private const val MIN_PROGRESS = 0f
private const val MAX_PROGRESS = 1f

/** Horizontal bar showing how far along the quiz the player is. */
@Composable
fun QuiziaProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    LinearProgressIndicator(
        progress = { progress.coerceIn(MIN_PROGRESS, MAX_PROGRESS) },
        modifier = modifier.height(QuiziaTheme.sizes.progressBarHeight),
        color = QuiziaTheme.colorScheme.primary,
        trackColor = QuiziaTheme.colorScheme.surfaceVariant,
        strokeCap = StrokeCap.Round,
        gapSize = QuiziaTheme.sizes.elevationNone,
        drawStopIndicator = {},
    )
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaProgressBarPreview() {
    QuiziaTheme {
        QuiziaProgressBar(
            progress = 0.4f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(QuiziaTheme.spacing.medium),
        )
    }
}
