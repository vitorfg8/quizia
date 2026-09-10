package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

/** Grouped settings block: section label above a tonal surface, matching Material 3 lists. */
@Composable
fun QuiziaSettingsGroup(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = title,
            style = QuiziaTheme.typography.titleSmall,
            color = QuiziaTheme.colorScheme.primary,
            modifier = Modifier.padding(
                start = QuiziaTheme.spacing.medium,
                bottom = QuiziaTheme.spacing.small,
            ),
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = QuiziaTheme.shapes.large,
            color = QuiziaTheme.colorScheme.surface,
            tonalElevation = QuiziaTheme.sizes.elevationNone,
        ) {
            Column(
                modifier = Modifier.padding(QuiziaTheme.spacing.medium),
                content = content,
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaSettingsGroupPreview() {
    QuiziaTheme {
        QuiziaSettingsGroup(
            title = "Theme",
            modifier = Modifier.padding(QuiziaTheme.spacing.medium),
        ) {
            Text(
                text = "System",
                style = QuiziaTheme.typography.bodyLarge,
                color = QuiziaTheme.colorScheme.onSurface,
            )
        }
    }
}
