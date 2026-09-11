package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    val contentColor = state.toContentColor()
    Surface(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = QuiziaTheme.sizes.optionCardMinHeight),
        enabled = enabled,
        shape = QuiziaTheme.shapes.large,
        color = state.toContainerColor(),
        contentColor = contentColor,
        border = BorderStroke(
            width = QuiziaTheme.sizes.borderHairline,
            color = state.toBorderColor(),
        ),
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
        modifier = Modifier.padding(QuiziaTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.small),
    ) {
        OptionRadio(state = state)
        Text(
            text = text,
            style = QuiziaTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
        if (state != OptionCardState.Default) {
            OptionResultMark(state = state)
        }
    }
}

@Composable
private fun OptionRadio(state: OptionCardState) {
    when (state) {
        OptionCardState.Default -> EmptyOptionRadio()
        OptionCardState.Correct -> FilledOptionMark(
            icon = Icons.Rounded.Check,
            containerColor = QuiziaTheme.extendedColors.success,
            contentColor = QuiziaTheme.extendedColors.onSuccess,
        )
        OptionCardState.Wrong -> SelectedOptionRadio(
            containerColor = QuiziaTheme.extendedColors.error,
            contentColor = QuiziaTheme.extendedColors.onError,
        )
    }
}

@Composable
private fun EmptyOptionRadio() {
    Box(
        modifier = Modifier
            .size(QuiziaTheme.sizes.iconMedium)
            .clip(CircleShape)
            .border(
                width = QuiziaTheme.sizes.borderHairline,
                color = QuiziaTheme.colorScheme.outline,
                shape = CircleShape,
            ),
    )
}

@Composable
private fun SelectedOptionRadio(
    containerColor: Color,
    contentColor: Color,
) {
    Box(
        modifier = Modifier
            .size(QuiziaTheme.sizes.iconMedium)
            .clip(CircleShape)
            .background(containerColor),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(QuiziaTheme.sizes.radioDot)
                .clip(CircleShape)
                .background(contentColor),
        )
    }
}

@Composable
private fun OptionResultMark(state: OptionCardState) {
    val isCorrect = state == OptionCardState.Correct
    Icon(
        imageVector = if (isCorrect) Icons.Rounded.Check else Icons.Rounded.Close,
        contentDescription = null,
        modifier = Modifier.size(QuiziaTheme.sizes.iconMedium),
        tint = if (isCorrect) {
            QuiziaTheme.extendedColors.success
        } else {
            QuiziaTheme.extendedColors.error
        },
    )
}

@Composable
private fun FilledOptionMark(
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
) {
    Box(
        modifier = Modifier
            .size(QuiziaTheme.sizes.iconMedium)
            .clip(CircleShape)
            .background(containerColor),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(QuiziaTheme.sizes.iconSmall),
            tint = contentColor,
        )
    }
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
private fun OptionCardState.toBorderColor(): Color = when (this) {
    OptionCardState.Default -> QuiziaTheme.colorScheme.outline
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
