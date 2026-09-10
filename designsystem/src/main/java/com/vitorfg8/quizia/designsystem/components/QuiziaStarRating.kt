package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

const val QUIZIA_MAX_STARS = 5

/**
 * Row of stars used to summarise a quiz result.
 * The individual icons are hidden from accessibility services; [contentDescription] describes
 * the whole rating in one sentence instead.
 */
@Composable
fun QuiziaStarRating(
    filledStars: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    totalStars: Int = QUIZIA_MAX_STARS,
) {
    Row(
        modifier = modifier.clearAndSetSemantics {
            this.contentDescription = contentDescription
        },
        horizontalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.extraSmall),
    ) {
        repeat(totalStars) { index ->
            val isFilled = index < filledStars
            Icon(
                imageVector = if (isFilled) Icons.Rounded.Star else Icons.Rounded.StarOutline,
                contentDescription = null,
                tint = if (isFilled) {
                    QuiziaTheme.colorScheme.primary
                } else {
                    QuiziaTheme.colorScheme.surfaceVariant
                },
                modifier = Modifier.size(QuiziaTheme.sizes.starLarge),
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaStarRatingPreview() {
    QuiziaTheme {
        Column(
            modifier = Modifier.padding(QuiziaTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.small),
        ) {
            listOf(0, 3, QUIZIA_MAX_STARS).forEach { filledStars ->
                QuiziaStarRating(
                    filledStars = filledStars,
                    contentDescription = "$filledStars of $QUIZIA_MAX_STARS stars",
                )
            }
        }
    }
}
