package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme
import com.vitorfg8.quizia.designsystem.icons.QuiziaIcons

/** A settings block: purple icon, title, one line of guidance, then the controls. */
@Composable
fun QuiziaSettingsSection(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.small),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.small),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(QuiziaTheme.sizes.iconMedium),
                tint = QuiziaTheme.colorScheme.primary,
            )
            Text(
                text = title,
                style = QuiziaTheme.typography.titleMedium,
                color = QuiziaTheme.colorScheme.onBackground,
            )
        }
        Text(
            text = description,
            style = QuiziaTheme.typography.bodyMedium,
            color = QuiziaTheme.colorScheme.onSurfaceVariant,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = QuiziaTheme.spacing.extraSmall),
            verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.small),
            content = content,
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaSettingsSectionPreview() {
    QuiziaTheme {
        QuiziaSettingsSection(
            icon = QuiziaIcons.Key,
            title = "API key",
            description = "Paste the key from your AI provider here.",
            modifier = Modifier.padding(QuiziaTheme.spacing.medium),
        ) {
            QuiziaTextField(
                value = "",
                onValueChange = {},
                placeholder = "Enter your API key",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
