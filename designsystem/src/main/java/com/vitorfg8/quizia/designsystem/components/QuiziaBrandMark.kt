package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

/** Rounded purple tile with the Quizia sparkle, used as the app's visual signature. */
@Composable
fun QuiziaBrandMark(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(QuiziaTheme.sizes.brandMark)
            .background(
                brush = Brush.linearGradient(QuiziaTheme.extendedColors.brandGradient),
                shape = QuiziaTheme.shapes.large,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Rounded.AutoAwesome,
            contentDescription = null,
            modifier = Modifier.size(QuiziaTheme.sizes.iconLarge),
            tint = QuiziaTheme.extendedColors.onBrandGradient,
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaBrandMarkPreview() {
    QuiziaTheme {
        QuiziaBrandMark()
    }
}
