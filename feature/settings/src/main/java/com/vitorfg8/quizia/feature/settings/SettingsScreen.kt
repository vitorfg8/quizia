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
import androidx.compose.material.icons.automirrored.rounded.HelpOutline
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Palette
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
import com.vitorfg8.quizia.designsystem.components.QuiziaAlertDialog
import com.vitorfg8.quizia.designsystem.components.QuiziaButton
import com.vitorfg8.quizia.designsystem.components.QuiziaChoiceChips
import com.vitorfg8.quizia.designsystem.components.QuiziaPasswordTextField
import com.vitorfg8.quizia.designsystem.components.QuiziaRadioOption
import com.vitorfg8.quizia.designsystem.components.QuiziaSettingsSection
import com.vitorfg8.quizia.designsystem.components.QuiziaTextButton
import com.vitorfg8.quizia.designsystem.components.QuiziaTextField
import com.vitorfg8.quizia.designsystem.components.QuiziaTopBar
import org.koin.androidx.compose.koinViewModel

private val THEME_OPTIONS = listOf(AppTheme.SYSTEM, AppTheme.LIGHT, AppTheme.DARK)

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
                .padding(horizontal = QuiziaTheme.spacing.large)
                .padding(
                    top = QuiziaTheme.spacing.small,
                    bottom = QuiziaTheme.spacing.extraLarge,
                ),
            verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.extraLarge),
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
                    onDeleteApiKeyClick = onDeleteApiKeyClick,
                )
            }
            QuestionCountSection(
                selectedCount = uiState.questionCount,
                onQuestionCountSelected = onQuestionCountSelected,
            )
            ThemeSection(
                selectedTheme = uiState.theme,
                onThemeSelected = onThemeSelected,
            )
            if (uiState.isApiKeyRequired && uiState.isEditingApiKey) {
                QuiziaButton(
                    text = stringResource(id = R.string.settings_save),
                    onClick = onSaveApiKeyClick,
                    enabled = uiState.canSaveApiKey,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
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
    QuiziaSettingsSection(
        icon = Icons.Rounded.AutoAwesome,
        title = stringResource(id = R.string.settings_section_provider),
        description = stringResource(id = R.string.settings_section_provider_description),
    ) {
        providers.forEach { provider ->
            QuiziaRadioOption(
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
    onDeleteApiKeyClick: () -> Unit,
) {
    QuiziaSettingsSection(
        icon = Icons.Rounded.Key,
        title = stringResource(id = R.string.settings_section_api_key),
        description = stringResource(id = R.string.settings_section_api_key_description),
    ) {
        if (uiState.isEditingApiKey) {
            QuiziaPasswordTextField(
                value = uiState.draftApiKey,
                onValueChange = onDraftApiKeyChanged,
                placeholder = stringResource(id = R.string.settings_api_key_placeholder),
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
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

@Composable
private fun QuestionCountSection(
    selectedCount: Int,
    onQuestionCountSelected: (Int) -> Unit,
) {
    QuiziaSettingsSection(
        icon = Icons.AutoMirrored.Rounded.HelpOutline,
        title = stringResource(id = R.string.settings_section_questions),
        description = stringResource(id = R.string.settings_section_questions_description),
    ) {
        QuiziaChoiceChips(
            options = QUESTION_COUNT_OPTIONS.map { count -> count.toQuestionCountLabel() },
            selectedIndex = QUESTION_COUNT_OPTIONS.indexOf(selectedCount).coerceAtLeast(0),
            onOptionSelected = { index -> onQuestionCountSelected(QUESTION_COUNT_OPTIONS[index]) },
        )
    }
}

@Composable
private fun ThemeSection(
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
) {
    QuiziaSettingsSection(
        icon = Icons.Rounded.Palette,
        title = stringResource(id = R.string.settings_section_theme),
        description = stringResource(id = R.string.settings_section_theme_description),
    ) {
        THEME_OPTIONS.forEach { theme ->
            QuiziaRadioOption(
                label = stringResource(id = theme.labelResId()),
                description = theme.descriptionResId()?.let { stringResource(id = it) },
                selected = theme == selectedTheme,
                onClick = { onThemeSelected(theme) },
            )
        }
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

private fun AppTheme.descriptionResId(): Int? = when (this) {
    AppTheme.SYSTEM -> R.string.settings_theme_system_description
    else -> null
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

@Preview(name = "Editing key – Light", showBackground = true)
@Preview(name = "Editing key – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun SettingsScreenEditingKeyPreview() {
    QuiziaTheme {
        SettingsScreen(
            uiState = SettingsUiState(
                selectedProvider = LlmProviderType.OPENAI,
                theme = AppTheme.DARK,
                questionCount = DEFAULT_QUESTION_COUNT,
                availableProviders = listOf(LlmProviderType.GEMINI_API, LlmProviderType.OPENAI),
                isEditingApiKey = true,
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
