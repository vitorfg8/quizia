package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

/** Single-choice row used for small, exclusive option sets such as theme and question count. */
@Composable
fun QuiziaSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                selected = index == selectedIndex,
                onClick = { onOptionSelected(index) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size,
                ),
                label = {
                    Text(
                        text = label,
                        style = QuiziaTheme.typography.labelLarge,
                    )
                },
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = QuiziaTheme.colorScheme.primary,
                    activeContentColor = QuiziaTheme.colorScheme.onPrimary,
                    inactiveContainerColor = QuiziaTheme.colorScheme.surfaceVariant,
                    inactiveContentColor = QuiziaTheme.colorScheme.onSurfaceVariant,
                ),
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaSegmentedControlPreview() {
    QuiziaTheme {
        QuiziaSegmentedControl(
            options = listOf("Light", "Dark", "System"),
            selectedIndex = 2,
            onOptionSelected = {},
            modifier = Modifier.padding(QuiziaTheme.spacing.medium),
        )
    }
}
