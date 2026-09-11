package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

/** Secondary pill action: purple outline, no fill, sits under the primary CTA. */
@Composable
fun QuiziaOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(QuiziaTheme.sizes.buttonHeight),
        enabled = enabled,
        shape = QuiziaTheme.shapes.extraLarge,
        border = BorderStroke(
            width = QuiziaTheme.sizes.borderHairline,
            color = if (enabled) {
                QuiziaTheme.colorScheme.primary
            } else {
                QuiziaTheme.colorScheme.outline
            },
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = QuiziaTheme.colorScheme.primary,
            disabledContentColor = QuiziaTheme.colorScheme.onSurfaceVariant,
        ),
    ) {
        ButtonLabel(text = text, leadingIcon = leadingIcon)
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaOutlinedButtonPreview() {
    QuiziaTheme {
        QuiziaOutlinedButton(
            text = "Back to home",
            onClick = {},
            leadingIcon = Icons.Rounded.Home,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
