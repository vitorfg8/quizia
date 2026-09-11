package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

/** One line in the results stats card: an icon and label on the left, the value on the right. */
@Composable
fun QuiziaStatRow(
    icon: ImageVector,
    label: String,
    value: String,
    iconTint: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.small),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(QuiziaTheme.sizes.iconMedium),
                tint = iconTint,
            )
            Text(
                text = label,
                style = QuiziaTheme.typography.bodyLarge,
                color = QuiziaTheme.colorScheme.onSurface,
            )
        }
        Text(
            text = value,
            style = QuiziaTheme.typography.titleMedium,
            color = QuiziaTheme.colorScheme.onSurface,
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaStatRowPreview() {
    QuiziaTheme {
        Column(
            modifier = Modifier.padding(QuiziaTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.medium),
        ) {
            QuiziaStatRow(
                icon = Icons.Rounded.CheckCircle,
                label = "Correct",
                value = "8",
                iconTint = QuiziaTheme.extendedColors.success,
            )
            HorizontalDivider(color = QuiziaTheme.colorScheme.outlineVariant)
            QuiziaStatRow(
                icon = Icons.Rounded.Schedule,
                label = "Time",
                value = "6 min 32 s",
                iconTint = QuiziaTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
