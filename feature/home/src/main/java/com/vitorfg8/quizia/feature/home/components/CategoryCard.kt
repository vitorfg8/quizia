package com.vitorfg8.quizia.feature.home.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiObjects
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

/** Entry point to a quiz: a soft card with the category's own accent on the icon. */
@Composable
fun CategoryCard(
    icon: ImageVector,
    label: String,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(QuiziaTheme.sizes.categoryCardHeight),
        shape = QuiziaTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = QuiziaTheme.colorScheme.surface,
            contentColor = QuiziaTheme.colorScheme.onSurface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = QuiziaTheme.sizes.elevationNone),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(QuiziaTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(QuiziaTheme.sizes.iconLarge),
                tint = iconTint,
            )
            Spacer(modifier = Modifier.padding(top = QuiziaTheme.spacing.small))
            Text(
                text = label,
                style = QuiziaTheme.typography.labelLarge,
                color = QuiziaTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CategoryCardPreview() {
    QuiziaTheme {
        CategoryCard(
            icon = Icons.Rounded.EmojiObjects,
            label = "General Knowledge",
            iconTint = QuiziaTheme.extendedColors.categoryViolet,
            onClick = {},
            modifier = Modifier.fillMaxWidth(0.45f),
        )
    }
}
