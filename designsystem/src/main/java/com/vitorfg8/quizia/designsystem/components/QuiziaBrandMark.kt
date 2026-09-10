package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

/** Rounded brand tile used on the welcome screen. */
@Composable
fun QuiziaBrandMark(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.size(QuiziaTheme.sizes.brandMark),
        shape = QuiziaTheme.shapes.large,
        color = QuiziaTheme.colorScheme.primary,
        contentColor = QuiziaTheme.colorScheme.onPrimary,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Rounded.Bolt,
                contentDescription = null,
                modifier = Modifier.size(QuiziaTheme.sizes.iconLarge),
            )
        }
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
