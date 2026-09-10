package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

@Composable
fun QuiziaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(QuiziaTheme.sizes.buttonHeight),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = QuiziaTheme.colorScheme.primary,
            contentColor = QuiziaTheme.colorScheme.onPrimary,
            disabledContainerColor = QuiziaTheme.colorScheme.surfaceVariant,
            disabledContentColor = QuiziaTheme.colorScheme.onSurfaceVariant,
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = QuiziaTheme.sizes.elevationNone,
            pressedElevation = QuiziaTheme.sizes.elevationNone,
            disabledElevation = QuiziaTheme.sizes.elevationNone,
        ),
        shape = QuiziaTheme.shapes.extraLarge,
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
private fun QuiziaButtonPreview() {
    QuiziaTheme {
        QuiziaButton(
            text = "Continue",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(name = "Disabled – Light", showBackground = true)
@Preview(name = "Disabled – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaButtonDisabledPreview() {
    QuiziaTheme {
        QuiziaButton(
            text = "Continue",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
        )
    }
}
