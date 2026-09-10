package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

/**
 * Material 3 basic dialog: headline, supporting text and trailing text buttons.
 * Use [isDestructive] when the confirm action cannot be undone.
 */
@Composable
fun QuiziaAlertDialog(
    title: String,
    text: String,
    confirmLabel: String,
    dismissLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    isDestructive: Boolean = false,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = QuiziaTheme.typography.headlineSmall,
                color = QuiziaTheme.colorScheme.onSurface,
            )
        },
        text = {
            Text(
                text = text,
                style = QuiziaTheme.typography.bodyMedium,
                color = QuiziaTheme.colorScheme.onSurfaceVariant,
            )
        },
        confirmButton = {
            QuiziaTextButton(
                text = confirmLabel,
                onClick = onConfirm,
                isDestructive = isDestructive,
            )
        },
        dismissButton = {
            QuiziaTextButton(
                text = dismissLabel,
                onClick = onDismiss,
            )
        },
        shape = QuiziaTheme.shapes.extraLarge,
        containerColor = QuiziaTheme.colorScheme.surface,
        tonalElevation = QuiziaTheme.sizes.elevationRaised,
    )
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaAlertDialogPreview() {
    QuiziaTheme {
        QuiziaAlertDialog(
            title = "Delete API key?",
            text = "The stored key will be removed from this device. You can add another one later.",
            confirmLabel = "Delete",
            dismissLabel = "Cancel",
            onConfirm = {},
            onDismiss = {},
            isDestructive = true,
        )
    }
}
