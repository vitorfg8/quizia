package com.vitorfg8.quizia.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuiziaTopBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = QuiziaTheme.typography.titleLarge,
                color = QuiziaTheme.colorScheme.onSurface,
            )
        },
        navigationIcon = navigationIcon,
        actions = actions,
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = QuiziaTheme.colorScheme.background,
            actionIconContentColor = QuiziaTheme.colorScheme.onSurfaceVariant,
        ),
    )
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuiziaTopBarPreview() {
    QuiziaTheme {
        QuiziaTopBar(title = "Quizia")
    }
}
