package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme
import com.vitorfg8.quizia.designsystem.icons.QuiziaIcons

/** Banner shown after an answer, naming whether it was right and why. */
@Composable
fun QuiziaAnswerFeedback(
    isCorrect: Boolean,
    title: String,
    explanation: String,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (isCorrect) {
        QuiziaTheme.extendedColors.successContainer
    } else {
        QuiziaTheme.extendedColors.errorContainer
    }
    val contentColor = if (isCorrect) {
        QuiziaTheme.extendedColors.onSuccessContainer
    } else {
        QuiziaTheme.extendedColors.onErrorContainer
    }
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = QuiziaTheme.shapes.large,
        color = containerColor,
        contentColor = contentColor,
    ) {
        Row(
            modifier = Modifier.padding(QuiziaTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.medium),
        ) {
            FeedbackGlyph(isCorrect = isCorrect)
            Column(verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.extraSmall)) {
                Text(text = title, style = QuiziaTheme.typography.titleSmall)
                Text(text = explanation, style = QuiziaTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun FeedbackGlyph(isCorrect: Boolean) {
    val glyphColor = if (isCorrect) {
        QuiziaTheme.extendedColors.success
    } else {
        QuiziaTheme.extendedColors.error
    }
    val onGlyphColor = if (isCorrect) {
        QuiziaTheme.extendedColors.onSuccess
    } else {
        QuiziaTheme.extendedColors.onError
    }
    Box(
        modifier = Modifier
            .size(QuiziaTheme.sizes.iconLarge)
            .clip(CircleShape)
            .background(glyphColor),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = if (isCorrect) QuiziaIcons.Check else QuiziaIcons.Close,
            contentDescription = null,
            modifier = Modifier.size(QuiziaTheme.sizes.iconMedium),
            tint = onGlyphColor,
        )
    }
}

@Preview(name = "Correct – Light", showBackground = true)
@Preview(name = "Correct – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaAnswerFeedbackCorrectPreview() {
    QuiziaTheme {
        QuiziaAnswerFeedback(
            isCorrect = true,
            title = "Correct!",
            explanation = "Tokyo is the capital of Japan.",
            modifier = Modifier.padding(QuiziaTheme.spacing.medium),
        )
    }
}

@Preview(name = "Wrong – Light", showBackground = true)
@Preview(name = "Wrong – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaAnswerFeedbackWrongPreview() {
    QuiziaTheme {
        QuiziaAnswerFeedback(
            isCorrect = false,
            title = "Incorrect!",
            explanation = "The capital of Japan is Tokyo.",
            modifier = Modifier.padding(QuiziaTheme.spacing.medium),
        )
    }
}
