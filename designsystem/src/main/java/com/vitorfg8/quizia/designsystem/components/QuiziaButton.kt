package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

/** Primary pill action, filled with the brand gradient while enabled. */
@Composable
fun QuiziaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val shape = QuiziaTheme.shapes.extraLarge
    val fill = if (enabled) {
        Brush.horizontalGradient(QuiziaTheme.extendedColors.brandGradient)
    } else {
        SolidColor(QuiziaTheme.colorScheme.surfaceVariant)
    }
    Button(
        onClick = onClick,
        modifier = modifier
            .height(QuiziaTheme.sizes.buttonHeight)
            .background(brush = fill, shape = shape),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = QuiziaTheme.extendedColors.onBrandGradient,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = QuiziaTheme.colorScheme.onSurfaceVariant,
        ),
        elevation = null,
        shape = shape,
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
