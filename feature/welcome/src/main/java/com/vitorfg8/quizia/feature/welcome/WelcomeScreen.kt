package com.vitorfg8.quizia.feature.welcome

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme
import com.vitorfg8.quizia.designsystem.components.QuiziaButton

@Composable
fun WelcomeScreen(
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = QuiziaTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = QuiziaTheme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Rounded.Psychology,
                contentDescription = null,
                modifier = Modifier.size(QuiziaTheme.sizes.iconHuge),
                tint = QuiziaTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.padding(top = QuiziaTheme.spacing.large))

            Text(
                text = stringResource(id = R.string.welcome_title),
                style = QuiziaTheme.typography.displaySmall,
                color = QuiziaTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.padding(top = QuiziaTheme.spacing.small))

            Text(
                text = stringResource(id = R.string.welcome_tagline),
                style = QuiziaTheme.typography.bodyLarge,
                color = QuiziaTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.weight(1f))

            QuiziaButton(
                text = stringResource(id = R.string.welcome_action_continue),
                onClick = onContinueClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = QuiziaTheme.spacing.extraLarge),
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun WelcomeScreenPreview() {
    QuiziaTheme {
        WelcomeScreen(onContinueClick = {})
    }
}
