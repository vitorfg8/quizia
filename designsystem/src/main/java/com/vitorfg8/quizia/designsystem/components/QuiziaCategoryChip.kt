package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme
import com.vitorfg8.quizia.designsystem.icons.QuiziaIcons

/** Compact pill that names the quiz category without competing with the question. */
@Composable
fun QuiziaCategoryChip(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.height(QuiziaTheme.sizes.categoryChipHeight),
        shape = QuiziaTheme.shapes.extraLarge,
        color = QuiziaTheme.colorScheme.primaryContainer,
        contentColor = QuiziaTheme.colorScheme.primary,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = QuiziaTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.extraSmall),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(QuiziaTheme.sizes.iconSmall),
            )
            Text(text = label, style = QuiziaTheme.typography.labelLarge)
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaCategoryChipPreview() {
    QuiziaTheme {
        QuiziaCategoryChip(
            icon = QuiziaIcons.Lightbulb,
            label = "General Knowledge",
            modifier = Modifier.padding(QuiziaTheme.spacing.medium),
        )
    }
}
