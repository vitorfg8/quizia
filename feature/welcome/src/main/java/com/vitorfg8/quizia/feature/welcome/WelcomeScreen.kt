package com.vitorfg8.quizia.feature.welcome

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.vitorfg8.quizia.designsystem.QuiziaTheme
import com.vitorfg8.quizia.designsystem.components.QuiziaBrandMark
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
            WelcomeHeader()
            Spacer(modifier = Modifier.padding(top = QuiziaTheme.spacing.extraLarge))
            WelcomeBenefits()
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

@Composable
private fun WelcomeHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.medium),
    ) {
        QuiziaBrandMark()
        Text(
            text = stringResource(id = R.string.welcome_title),
            style = QuiziaTheme.typography.displaySmall,
            color = QuiziaTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(id = R.string.welcome_tagline),
            style = QuiziaTheme.typography.bodyLarge,
            color = QuiziaTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun WelcomeBenefits(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.large),
    ) {
        WelcomeBenefit(
            icon = Icons.Rounded.AutoAwesome,
            titleResId = R.string.welcome_benefit_questions_title,
            subtitleResId = R.string.welcome_benefit_questions_subtitle,
        )
        WelcomeBenefit(
            icon = Icons.Rounded.Lightbulb,
            titleResId = R.string.welcome_benefit_practice_title,
            subtitleResId = R.string.welcome_benefit_practice_subtitle,
        )
        WelcomeBenefit(
            icon = Icons.Rounded.Tune,
            titleResId = R.string.welcome_benefit_pace_title,
            subtitleResId = R.string.welcome_benefit_pace_subtitle,
        )
    }
}

@Composable
private fun WelcomeBenefit(
    icon: ImageVector,
    @StringRes titleResId: Int,
    @StringRes subtitleResId: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.medium),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(QuiziaTheme.sizes.iconMedium),
            tint = QuiziaTheme.colorScheme.primary,
        )
        Column(verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.extraSmall)) {
            Text(
                text = stringResource(id = titleResId),
                style = QuiziaTheme.typography.titleSmall,
                color = QuiziaTheme.colorScheme.onBackground,
            )
            Text(
                text = stringResource(id = subtitleResId),
                style = QuiziaTheme.typography.bodyMedium,
                color = QuiziaTheme.colorScheme.onSurfaceVariant,
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
