package com.vitorfg8.quizia.feature.llmsetup

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.designsystem.QuiziaTheme
import com.vitorfg8.quizia.designsystem.components.LlmProviderRadioItem
import com.vitorfg8.quizia.designsystem.components.QuiziaButton
import com.vitorfg8.quizia.designsystem.components.QuiziaPasswordTextField
import org.koin.androidx.compose.koinViewModel

@Composable
fun LlmSetupRoute(
    onSetupComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LlmSetupViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                LlmSetupSideEffect.NavigateToHome -> onSetupComplete()
            }
        }
    }
    LlmSetupScreen(
        uiState = uiState,
        onProviderSelected = viewModel::selectProvider,
        onApiKeyChanged = viewModel::updateApiKey,
        onContinueClick = viewModel::onContinueClick,
        modifier = modifier,
    )
}

@Composable
internal fun LlmSetupScreen(
    uiState: LlmSetupUiState,
    onProviderSelected: (LlmProviderType) -> Unit,
    onApiKeyChanged: (String) -> Unit,
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = QuiziaTheme.spacing.large),
        ) {
            Spacer(modifier = Modifier.padding(top = QuiziaTheme.spacing.extraLarge))

            Text(
                text = stringResource(id = R.string.llm_setup_title),
                style = QuiziaTheme.typography.headlineMedium,
                color = QuiziaTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.padding(top = QuiziaTheme.spacing.small))

            Text(
                text = stringResource(id = R.string.llm_setup_subtitle),
                style = QuiziaTheme.typography.bodyLarge,
                color = QuiziaTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.padding(top = QuiziaTheme.spacing.large))

            HorizontalDivider(color = QuiziaTheme.colorScheme.outline)

            uiState.availableProviders.forEach { provider ->
                LlmProviderRadioItem(
                    label = stringResource(id = provider.labelResId()),
                    selected = uiState.selectedProvider == provider,
                    onClick = { onProviderSelected(provider) },
                )
                HorizontalDivider(color = QuiziaTheme.colorScheme.outline)
            }

            AnimatedVisibility(visible = uiState.isApiKeyRequired) {
                QuiziaPasswordTextField(
                    value = uiState.apiKey,
                    onValueChange = onApiKeyChanged,
                    label = stringResource(id = R.string.llm_setup_api_key_label),
                    placeholder = stringResource(id = R.string.llm_setup_api_key_placeholder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = QuiziaTheme.spacing.large),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            QuiziaButton(
                text = stringResource(id = R.string.llm_setup_action_continue),
                onClick = onContinueClick,
                enabled = uiState.canContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = QuiziaTheme.spacing.extraLarge),
            )
        }
    }
}

private fun LlmProviderType.labelResId(): Int = when (this) {
    LlmProviderType.GEMINI_NANO -> R.string.llm_provider_gemini_nano
    LlmProviderType.GEMINI_API -> R.string.llm_provider_gemini_api
    LlmProviderType.OPENAI -> R.string.llm_provider_openai
    LlmProviderType.CLAUDE -> R.string.llm_provider_claude
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun LlmSetupScreenPreview() {
    QuiziaTheme {
        LlmSetupScreen(
            uiState = LlmSetupUiState(
                availableProviders = listOf(
                    LlmProviderType.GEMINI_API,
                    LlmProviderType.OPENAI,
                    LlmProviderType.CLAUDE,
                ),
                selectedProvider = LlmProviderType.GEMINI_API,
                isApiKeyRequired = true,
                canContinue = false,
            ),
            onProviderSelected = {},
            onApiKeyChanged = {},
            onContinueClick = {},
        )
    }
}

@Preview(name = "API Key Filled – Light", showBackground = true)
@Preview(name = "API Key Filled – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun LlmSetupScreenWithKeyPreview() {
    QuiziaTheme {
        LlmSetupScreen(
            uiState = LlmSetupUiState(
                availableProviders = listOf(
                    LlmProviderType.GEMINI_API,
                    LlmProviderType.OPENAI,
                    LlmProviderType.CLAUDE,
                ),
                selectedProvider = LlmProviderType.OPENAI,
                apiKey = "sk-secret",
                isApiKeyRequired = true,
                canContinue = true,
            ),
            onProviderSelected = {},
            onApiKeyChanged = {},
            onContinueClick = {},
        )
    }
}
