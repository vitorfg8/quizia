package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

/** How an answer option should read once the quiz has judged it. */
enum class OptionCardState {
    Default,
    Correct,
    Wrong,
}

/** Tappable answer option that turns green or red after the answer is revealed. */
@Composable
fun QuiziaOptionCard(
    text: String,
    state: OptionCardState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = QuiziaTheme.sizes.optionCardMinHeight),
        enabled = enabled,
        shape = QuiziaTheme.shapes.large,
        color = state.toContainerColor(),
        contentColor = state.toContentColor(),
    ) {
        Row(
            modifier = Modifier.padding(QuiziaTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.medium),
        ) {
            Text(
                text = text,
                style = QuiziaTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
            )
            state.toTrailingIcon()?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(QuiziaTheme.sizes.iconMedium),
                )
            }
        }
    }
}

@Composable
private fun OptionCardState.toContainerColor(): Color = when (this) {
    OptionCardState.Default -> QuiziaTheme.colorScheme.surfaceVariant
    OptionCardState.Correct -> QuiziaTheme.extendedColors.correctAnswer
    OptionCardState.Wrong -> QuiziaTheme.extendedColors.wrongAnswer
}

@Composable
private fun OptionCardState.toContentColor(): Color = when (this) {
    OptionCardState.Default -> QuiziaTheme.colorScheme.onSurfaceVariant
    OptionCardState.Correct -> QuiziaTheme.extendedColors.onCorrectAnswer
    OptionCardState.Wrong -> QuiziaTheme.extendedColors.onWrongAnswer
}

private fun OptionCardState.toTrailingIcon(): ImageVector? = when (this) {
    OptionCardState.Default -> null
    OptionCardState.Correct -> Icons.Rounded.Check
    OptionCardState.Wrong -> Icons.Rounded.Close
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaOptionCardPreview() {
    QuiziaTheme {
        Column(
            modifier = Modifier.padding(QuiziaTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.small),
        ) {
            OptionCardState.entries.forEach { state ->
                QuiziaOptionCard(
                    text = state.name,
                    state = state,
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
