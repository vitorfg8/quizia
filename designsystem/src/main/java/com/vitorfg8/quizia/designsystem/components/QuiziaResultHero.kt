package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

private data class HeroSpark(
    val x: Float,
    val y: Float,
    val radius: Float,
    val alpha: Float,
)

private val HERO_SPARKS = listOf(
    HeroSpark(x = 0.18f, y = 0.22f, radius = 0.035f, alpha = 0.55f),
    HeroSpark(x = 0.82f, y = 0.18f, radius = 0.028f, alpha = 0.40f),
    HeroSpark(x = 0.88f, y = 0.52f, radius = 0.022f, alpha = 0.35f),
    HeroSpark(x = 0.14f, y = 0.68f, radius = 0.030f, alpha = 0.45f),
    HeroSpark(x = 0.78f, y = 0.80f, radius = 0.018f, alpha = 0.30f),
    HeroSpark(x = 0.50f, y = 0.08f, radius = 0.016f, alpha = 0.25f),
)

/** Large check or close that announces whether the quiz went well, with a few quiet sparks. */
@Composable
fun QuiziaResultHero(
    isSuccess: Boolean,
    modifier: Modifier = Modifier,
) {
    val accent = if (isSuccess) {
        QuiziaTheme.extendedColors.success
    } else {
        QuiziaTheme.extendedColors.error
    }
    val onAccent = if (isSuccess) {
        QuiziaTheme.extendedColors.onSuccess
    } else {
        QuiziaTheme.extendedColors.onError
    }
    Box(
        modifier = modifier.size(QuiziaTheme.sizes.resultHeroSize),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            HERO_SPARKS.forEach { spark -> drawHeroSpark(spark = spark, color = accent) }
        }
        Box(
            modifier = Modifier
                .size(QuiziaTheme.sizes.iconHuge)
                .clip(CircleShape)
                .background(accent),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = if (isSuccess) Icons.Rounded.Check else Icons.Rounded.Close,
                contentDescription = null,
                modifier = Modifier.size(QuiziaTheme.sizes.iconLarge),
                tint = onAccent,
            )
        }
    }
}

private fun DrawScope.drawHeroSpark(spark: HeroSpark, color: Color) {
    drawCircle(
        color = color.copy(alpha = spark.alpha),
        radius = spark.radius * size.minDimension,
        center = Offset(x = spark.x * size.width, y = spark.y * size.height),
    )
}

@Preview(name = "Success – Light", showBackground = true)
@Preview(name = "Success – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaResultHeroSuccessPreview() {
    QuiziaTheme {
        QuiziaResultHero(isSuccess = true, modifier = Modifier.padding(QuiziaTheme.spacing.medium))
    }
}

@Preview(name = "Failure – Light", showBackground = true)
@Preview(name = "Failure – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaResultHeroFailurePreview() {
    QuiziaTheme {
        QuiziaResultHero(isSuccess = false, modifier = Modifier.padding(QuiziaTheme.spacing.medium))
    }
}
