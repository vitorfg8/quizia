package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

/** Low-emphasis action used in dialogs and secondary settings actions. */
@Composable
fun QuiziaTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isDestructive: Boolean = false,
) {
    val contentColor = if (isDestructive) {
        QuiziaTheme.colorScheme.error
    } else {
        QuiziaTheme.colorScheme.primary
    }
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = contentColor,
            disabledContentColor = QuiziaTheme.colorScheme.onSurfaceVariant,
        ),
    ) {
        Text(
            text = text,
            style = QuiziaTheme.typography.labelLarge,
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaTextButtonPreview() {
    QuiziaTheme {
        Row(
            modifier = Modifier.padding(QuiziaTheme.spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.small),
        ) {
            QuiziaTextButton(text = "Cancel", onClick = {})
            QuiziaTextButton(text = "Delete", onClick = {}, isDestructive = true)
        }
    }
}
