package com.vitorfg8.quizia.feature.settings

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitorfg8.quizia.core.domain.model.AppTheme
import com.vitorfg8.quizia.core.domain.model.LlmProviderType
import com.vitorfg8.quizia.designsystem.QuiziaTheme
import com.vitorfg8.quizia.designsystem.components.LlmProviderRadioItem
import com.vitorfg8.quizia.designsystem.components.QuiziaAlertDialog
import com.vitorfg8.quizia.designsystem.components.QuiziaButton
import com.vitorfg8.quizia.designsystem.components.QuiziaPasswordTextField
import com.vitorfg8.quizia.designsystem.components.QuiziaSegmentedControl
import com.vitorfg8.quizia.designsystem.components.QuiziaSettingsGroup
import com.vitorfg8.quizia.designsystem.components.QuiziaTextButton
import com.vitorfg8.quizia.designsystem.components.QuiziaTextField
import com.vitorfg8.quizia.designsystem.components.QuiziaTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                SettingsSideEffect.NavigateBack -> onBack()
            }
        }
    }
    SettingsScreen(
        uiState = uiState,
        onProviderSelected = viewModel::selectProvider,
        onThemeSelected = viewModel::selectTheme,
        onQuestionCountSelected = viewModel::selectQuestionCount,
        onDraftApiKeyChanged = viewModel::updateDraftApiKey,
        onChangeApiKeyClick = viewModel::onChangeApiKeyClick,
        onSaveApiKeyClick = viewModel::saveApiKey,
        onDeleteApiKeyClick = viewModel::onDeleteApiKeyClick,
        onConfirmDeleteApiKey = viewModel::confirmDeleteApiKey,
        onDismissDeleteApiKey = viewModel::dismissDeleteApiKeyDialog,
        onBackClick = viewModel::onBackClick,
        modifier = modifier,
    )
}

@Composable
internal fun SettingsScreen(
    uiState: SettingsUiState,
    onProviderSelected: (LlmProviderType) -> Unit,
    onThemeSelected: (AppTheme) -> Unit,
    onQuestionCountSelected: (Int) -> Unit,
    onDraftApiKeyChanged: (String) -> Unit,
    onChangeApiKeyClick: () -> Unit,
    onSaveApiKeyClick: () -> Unit,
    onDeleteApiKeyClick: () -> Unit,
    onConfirmDeleteApiKey: () -> Unit,
    onDismissDeleteApiKey: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = QuiziaTheme.colorScheme.background,
        topBar = {
            QuiziaTopBar(
                title = stringResource(id = R.string.settings_title),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(
                                id = R.string.settings_back_content_description,
                            ),
                            tint = QuiziaTheme.colorScheme.onSurface,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = QuiziaTheme.spacing.medium)
                .padding(
                    top = QuiziaTheme.spacing.small,
                    bottom = QuiziaTheme.spacing.extraLarge,
                ),
            verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.large),
        ) {
            ProviderSection(
                providers = uiState.availableProviders,
                selectedProvider = uiState.selectedProvider,
                onProviderSelected = onProviderSelected,
            )
            if (uiState.isApiKeyRequired) {
                ApiKeySection(
                    uiState = uiState,
                    onDraftApiKeyChanged = onDraftApiKeyChanged,
                    onChangeApiKeyClick = onChangeApiKeyClick,
                    onSaveApiKeyClick = onSaveApiKeyClick,
                    onDeleteApiKeyClick = onDeleteApiKeyClick,
                )
            }
            ThemeSection(
                selectedTheme = uiState.theme,
                onThemeSelected = onThemeSelected,
            )
            QuestionCountSection(
                selectedCount = uiState.questionCount,
                onQuestionCountSelected = onQuestionCountSelected,
            )
        }
    }
    if (uiState.isDeleteKeyDialogVisible) {
        QuiziaAlertDialog(
            title = stringResource(id = R.string.settings_delete_key_title),
            text = stringResource(id = R.string.settings_delete_key_message),
            confirmLabel = stringResource(id = R.string.settings_delete_key_confirm),
            dismissLabel = stringResource(id = R.string.settings_cancel),
            onConfirm = onConfirmDeleteApiKey,
            onDismiss = onDismissDeleteApiKey,
            isDestructive = true,
        )
    }
}

@Composable
private fun ProviderSection(
    providers: List<LlmProviderType>,
    selectedProvider: LlmProviderType,
    onProviderSelected: (LlmProviderType) -> Unit,
) {
    QuiziaSettingsGroup(title = stringResource(id = R.string.settings_section_provider)) {
        providers.forEachIndexed { index, provider ->
            if (index > 0) {
                HorizontalDivider(color = QuiziaTheme.colorScheme.outlineVariant)
            }
            LlmProviderRadioItem(
                label = stringResource(id = provider.labelResId()),
                selected = selectedProvider == provider,
                onClick = { onProviderSelected(provider) },
            )
        }
    }
}

@Composable
private fun ApiKeySection(
    uiState: SettingsUiState,
    onDraftApiKeyChanged: (String) -> Unit,
    onChangeApiKeyClick: () -> Unit,
    onSaveApiKeyClick: () -> Unit,
    onDeleteApiKeyClick: () -> Unit,
) {
    QuiziaSettingsGroup(title = stringResource(id = R.string.settings_section_api_key)) {
        if (uiState.isEditingApiKey) {
            Column(verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.medium)) {
                QuiziaPasswordTextField(
                    value = uiState.draftApiKey,
                    onValueChange = onDraftApiKeyChanged,
                    label = stringResource(id = R.string.settings_api_key_label),
                    placeholder = stringResource(id = R.string.settings_api_key_placeholder),
                    modifier = Modifier.fillMaxWidth(),
                )
                QuiziaButton(
                    text = stringResource(id = R.string.settings_api_key_save),
                    onClick = onSaveApiKeyClick,
                    enabled = uiState.canSaveApiKey,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.small)) {
                QuiziaTextField(
                    value = uiState.apiKeyMasked,
                    onValueChange = {},
                    label = stringResource(id = R.string.settings_api_key_label),
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    QuiziaTextButton(
                        text = stringResource(id = R.string.settings_api_key_change),
                        onClick = onChangeApiKeyClick,
                    )
                    if (uiState.hasStoredApiKey) {
                        QuiziaTextButton(
                            text = stringResource(id = R.string.settings_api_key_delete),
                            onClick = onDeleteApiKeyClick,
                            isDestructive = true,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeSection(
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
) {
    val themes = AppTheme.entries
    QuiziaSettingsGroup(title = stringResource(id = R.string.settings_section_theme)) {
        QuiziaSegmentedControl(
            options = themes.map { theme -> stringResource(id = theme.labelResId()) },
            selectedIndex = themes.indexOf(selectedTheme),
            onOptionSelected = { index -> onThemeSelected(themes[index]) },
        )
    }
}

@Composable
private fun QuestionCountSection(
    selectedCount: Int,
    onQuestionCountSelected: (Int) -> Unit,
) {
    QuiziaSettingsGroup(title = stringResource(id = R.string.settings_section_questions)) {
        QuiziaSegmentedControl(
            options = QUESTION_COUNT_OPTIONS.map { count -> count.toQuestionCountLabel() },
            selectedIndex = QUESTION_COUNT_OPTIONS.indexOf(selectedCount).coerceAtLeast(0),
            onOptionSelected = { index -> onQuestionCountSelected(QUESTION_COUNT_OPTIONS[index]) },
        )
    }
}

@Composable
private fun Int.toQuestionCountLabel(): String = when (this) {
    QUESTION_COUNT_OPTIONS[0] -> stringResource(id = R.string.settings_question_count_5)
    QUESTION_COUNT_OPTIONS[1] -> stringResource(id = R.string.settings_question_count_10)
    else -> stringResource(id = R.string.settings_question_count_15)
}

private fun LlmProviderType.labelResId(): Int = when (this) {
    LlmProviderType.GEMINI_NANO -> R.string.llm_provider_gemini_nano
    LlmProviderType.GEMINI_API -> R.string.llm_provider_gemini_api
    LlmProviderType.OPENAI -> R.string.llm_provider_openai
    LlmProviderType.CLAUDE -> R.string.llm_provider_claude
}

private fun AppTheme.labelResId(): Int = when (this) {
    AppTheme.LIGHT -> R.string.settings_theme_light
    AppTheme.DARK -> R.string.settings_theme_dark
    AppTheme.SYSTEM -> R.string.settings_theme_system
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun SettingsScreenPreview() {
    QuiziaTheme {
        SettingsScreen(
            uiState = SettingsUiState(
                selectedProvider = LlmProviderType.GEMINI_API,
                apiKeyMasked = "••••••••1234",
                theme = AppTheme.SYSTEM,
                questionCount = DEFAULT_QUESTION_COUNT,
                availableProviders = listOf(
                    LlmProviderType.GEMINI_API,
                    LlmProviderType.OPENAI,
                    LlmProviderType.CLAUDE,
                ),
            ),
            onProviderSelected = {},
            onThemeSelected = {},
            onQuestionCountSelected = {},
            onDraftApiKeyChanged = {},
            onChangeApiKeyClick = {},
            onSaveApiKeyClick = {},
            onDeleteApiKeyClick = {},
            onConfirmDeleteApiKey = {},
            onDismissDeleteApiKey = {},
            onBackClick = {},
        )
    }
}

@Preview(name = "Delete dialog – Light", showBackground = true)
@Preview(name = "Delete dialog – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun SettingsScreenDeleteDialogPreview() {
    QuiziaTheme {
        SettingsScreen(
            uiState = SettingsUiState(
                selectedProvider = LlmProviderType.GEMINI_API,
                apiKeyMasked = "••••••••1234",
                theme = AppTheme.SYSTEM,
                questionCount = DEFAULT_QUESTION_COUNT,
                availableProviders = listOf(LlmProviderType.GEMINI_API),
                isDeleteKeyDialogVisible = true,
            ),
            onProviderSelected = {},
            onThemeSelected = {},
            onQuestionCountSelected = {},
            onDraftApiKeyChanged = {},
            onChangeApiKeyClick = {},
            onSaveApiKeyClick = {},
            onDeleteApiKeyClick = {},
            onConfirmDeleteApiKey = {},
            onDismissDeleteApiKey = {},
            onBackClick = {},
        )
    }
}
