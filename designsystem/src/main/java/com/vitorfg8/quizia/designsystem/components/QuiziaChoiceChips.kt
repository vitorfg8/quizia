package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

/** Row of pills for a short exclusive set of values, such as how many questions a quiz has. */
@Composable
fun QuiziaChoiceChips(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.small),
    ) {
        options.forEachIndexed { index, label ->
            ChoiceChip(
                label = label,
                selected = index == selectedIndex,
                onClick = { onOptionSelected(index) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun ChoiceChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .height(QuiziaTheme.sizes.chipHeight)
            .selectable(selected = selected, role = Role.RadioButton, onClick = onClick),
        shape = QuiziaTheme.shapes.extraLarge,
        color = if (selected) {
            QuiziaTheme.colorScheme.primary
        } else {
            QuiziaTheme.colorScheme.surface
        },
        contentColor = if (selected) {
            QuiziaTheme.colorScheme.onPrimary
        } else {
            QuiziaTheme.colorScheme.onSurfaceVariant
        },
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = label, style = QuiziaTheme.typography.labelLarge)
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaChoiceChipsPreview() {
    QuiziaTheme {
        QuiziaChoiceChips(
            options = listOf("5", "10", "15"),
            selectedIndex = 1,
            onOptionSelected = {},
            modifier = Modifier.padding(QuiziaTheme.spacing.medium),
        )
    }
}
