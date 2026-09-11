package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

/** Exclusive choice row. The selected one is tinted so the option reads at a glance. */
@Composable
fun QuiziaRadioOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .selectable(selected = selected, role = Role.RadioButton, onClick = onClick),
        shape = QuiziaTheme.shapes.large,
        color = if (selected) {
            QuiziaTheme.colorScheme.primaryContainer
        } else {
            QuiziaTheme.colorScheme.surface
        },
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = QuiziaTheme.spacing.medium,
                vertical = QuiziaTheme.spacing.small,
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.small),
        ) {
            RadioButton(
                selected = selected,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = QuiziaTheme.colorScheme.primary,
                    unselectedColor = QuiziaTheme.colorScheme.onSurfaceVariant,
                ),
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = QuiziaTheme.typography.bodyLarge,
                    color = QuiziaTheme.colorScheme.onSurface,
                )
                if (description != null) {
                    Text(
                        text = description,
                        style = QuiziaTheme.typography.bodyMedium,
                        color = QuiziaTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaRadioOptionPreview() {
    QuiziaTheme {
        Column(
            modifier = Modifier.padding(QuiziaTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.small),
        ) {
            QuiziaRadioOption(
                label = "System",
                description = "Follows your device theme",
                selected = true,
                onClick = {},
            )
            QuiziaRadioOption(label = "Light", selected = false, onClick = {})
        }
    }
}
