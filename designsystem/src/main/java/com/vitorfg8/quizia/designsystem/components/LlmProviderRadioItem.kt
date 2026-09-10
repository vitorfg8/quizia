package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

@Composable
fun LlmProviderRadioItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = QuiziaTheme.spacing.medium,
                vertical = QuiziaTheme.spacing.small,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = QuiziaTheme.colorScheme.primary,
                unselectedColor = QuiziaTheme.colorScheme.onSurfaceVariant,
            ),
        )
        Spacer(modifier = Modifier.width(QuiziaTheme.spacing.small))
        Text(
            text = label,
            style = QuiziaTheme.typography.bodyLarge,
            color = QuiziaTheme.colorScheme.onSurface,
        )
    }
}

@Preview(name = "Selected – Light", showBackground = true)
@Preview(name = "Selected – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun LlmProviderRadioItemSelectedPreview() {
    QuiziaTheme {
        LlmProviderRadioItem(label = "Gemini API", selected = true, onClick = {})
    }
}

@Preview(name = "Unselected – Light", showBackground = true)
@Preview(name = "Unselected – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun LlmProviderRadioItemUnselectedPreview() {
    QuiziaTheme {
        LlmProviderRadioItem(label = "Claude API", selected = false, onClick = {})
    }
}
