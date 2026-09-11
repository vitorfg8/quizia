package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        modifier = modifier.defaultMinSize(minHeight = QuiziaTheme.sizes.radioOptionMinHeight),
        enabled = enabled,
        shape = QuiziaTheme.shapes.large,
        color = state.toContainerColor(),
        contentColor = state.toContentColor(),
        tonalElevation = QuiziaTheme.sizes.elevationNone,
        shadowElevation = QuiziaTheme.sizes.elevationNone,
    ) {
        OptionCardContent(text = text, state = state)
    }
}

@Composable
private fun OptionCardContent(
    text: String,
    state: OptionCardState,
) {
    Row(
        modifier = Modifier.padding(
            horizontal = QuiziaTheme.spacing.medium,
            vertical = QuiziaTheme.spacing.small,
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.small),
    ) {
        OptionRadio(state = state)
        Text(
            text = text,
            style = QuiziaTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun OptionRadio(state: OptionCardState) {
    RadioButton(
        selected = state != OptionCardState.Default,
        onClick = null,
        colors = RadioButtonDefaults.colors(
            selectedColor = state.toRadioColor(),
            unselectedColor = QuiziaTheme.colorScheme.onSurfaceVariant,
        ),
    )
}

@Composable
private fun OptionCardState.toContainerColor(): Color = when (this) {
    OptionCardState.Default -> QuiziaTheme.colorScheme.surface
    OptionCardState.Correct -> QuiziaTheme.extendedColors.successContainer
    OptionCardState.Wrong -> QuiziaTheme.extendedColors.errorContainer
}

@Composable
private fun OptionCardState.toContentColor(): Color = when (this) {
    OptionCardState.Default -> QuiziaTheme.colorScheme.onSurface
    OptionCardState.Correct -> QuiziaTheme.extendedColors.onSuccessContainer
    OptionCardState.Wrong -> QuiziaTheme.extendedColors.onErrorContainer
}

@Composable
private fun OptionCardState.toRadioColor(): Color = when (this) {
    OptionCardState.Default -> QuiziaTheme.colorScheme.primary
    OptionCardState.Correct -> QuiziaTheme.extendedColors.success
    OptionCardState.Wrong -> QuiziaTheme.extendedColors.error
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
