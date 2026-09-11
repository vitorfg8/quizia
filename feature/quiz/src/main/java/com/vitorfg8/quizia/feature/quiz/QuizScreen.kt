package com.vitorfg8.quizia.feature.quiz

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.EmojiObjects
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Newspaper
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.SportsEsports
import androidx.compose.material.icons.rounded.SportsSoccer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitorfg8.quizia.core.domain.model.Question
import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.designsystem.QuiziaTheme
import com.vitorfg8.quizia.designsystem.components.OptionCardState
import com.vitorfg8.quizia.designsystem.components.QuiziaAnswerFeedback
import com.vitorfg8.quizia.designsystem.components.QuiziaButton
import com.vitorfg8.quizia.designsystem.components.QuiziaCategoryChip
import com.vitorfg8.quizia.designsystem.components.QuiziaOptionCard
import com.vitorfg8.quizia.designsystem.components.QuiziaProgressBar
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun QuizRoute(
    category: QuizCategory,
    onQuizFinished: (score: Int, total: Int, elapsedMs: Long) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = koinViewModel { parametersOf(category) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is QuizSideEffect.NavigateToResults -> onQuizFinished(
                    effect.score,
                    effect.total,
                    effect.elapsedMs,
                )
            }
        }
    }
    QuizScreen(
        uiState = uiState,
        category = category,
        onOptionClick = viewModel::selectOption,
        onNextClick = viewModel::goToNextQuestion,
        onRetryClick = viewModel::loadQuiz,
        onBackClick = onBack,
        modifier = modifier,
    )
}

@Composable
internal fun QuizScreen(
    uiState: QuizUiState,
    category: QuizCategory,
    onOptionClick: (Int) -> Unit,
    onNextClick: () -> Unit,
    onRetryClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = QuiziaTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = QuiziaTheme.spacing.large)
                .padding(bottom = QuiziaTheme.spacing.large),
            verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.medium),
        ) {
            when {
                uiState.isLoading -> LoadingContent(onBackClick = onBackClick)
                uiState.errorMessageResId != null -> ErrorContent(
                    messageResId = uiState.errorMessageResId,
                    onRetryClick = onRetryClick,
                    onBackClick = onBackClick,
                )
                uiState.question != null -> QuestionContent(
                    uiState = uiState,
                    category = category,
                    question = uiState.question,
                    onOptionClick = onOptionClick,
                    onNextClick = onNextClick,
                    onBackClick = onBackClick,
                )
            }
        }
    }
}

@Composable
private fun LoadingContent(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        QuizBackButton(onBackClick = onBackClick)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(
                space = QuiziaTheme.spacing.medium,
                alignment = Alignment.CenterVertically,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator(color = QuiziaTheme.colorScheme.primary)
            Text(
                text = stringResource(id = R.string.quiz_loading),
                style = QuiziaTheme.typography.bodyLarge,
                color = QuiziaTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun ErrorContent(
    @StringRes messageResId: Int,
    onRetryClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        QuizBackButton(onBackClick = onBackClick)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(
                space = QuiziaTheme.spacing.large,
                alignment = Alignment.CenterVertically,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = messageResId),
                style = QuiziaTheme.typography.bodyLarge,
                color = QuiziaTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            QuiziaButton(
                text = stringResource(id = R.string.quiz_retry),
                onClick = onRetryClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun QuestionContent(
    uiState: QuizUiState,
    category: QuizCategory,
    question: Question,
    onOptionClick: (Int) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isCorrect = uiState.selectedOptionIndex == question.correctIndex
    val explanation = question.explanation.ifBlank {
        stringResource(id = R.string.quiz_feedback_fallback, question.correctOption)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.large),
    ) {
        QuizProgressHeader(uiState = uiState, onBackClick = onBackClick)
        QuiziaCategoryChip(
            icon = category.toIcon(),
            label = stringResource(id = category.labelResId()),
        )
        Text(
            text = question.text,
            style = QuiziaTheme.typography.headlineSmall,
            color = QuiziaTheme.colorScheme.onBackground,
        )
        question.options.forEachIndexed { index, option ->
            QuiziaOptionCard(
                text = option,
                state = uiState.resolveOptionState(index),
                onClick = { onOptionClick(index) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.answerRevealed,
            )
        }
        AnimatedVisibility(visible = uiState.answerRevealed) {
            QuiziaAnswerFeedback(
                isCorrect = isCorrect,
                title = stringResource(
                    id = if (isCorrect) R.string.quiz_feedback_correct else R.string.quiz_feedback_incorrect,
                ),
                explanation = explanation,
            )
        }
        if (uiState.answerRevealed) {
            QuiziaButton(
                text = stringResource(
                    id = if (uiState.isLastQuestion) R.string.quiz_see_results else R.string.quiz_next,
                ),
                onClick = onNextClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun QuizProgressHeader(
    uiState: QuizUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val progressDescription = stringResource(
        id = R.string.quiz_progress_content_description,
        uiState.currentQuestionIndex + 1,
        uiState.totalQuestions,
    )
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.small),
    ) {
        QuizBackButton(onBackClick = onBackClick)
        QuiziaProgressBar(
            progress = uiState.progress,
            modifier = Modifier
                .weight(1f)
                .semantics { contentDescription = progressDescription },
        )
        Text(
            text = stringResource(
                id = R.string.quiz_title,
                uiState.currentQuestionIndex + 1,
                uiState.totalQuestions,
            ),
            style = QuiziaTheme.typography.labelLarge,
            color = QuiziaTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun QuizBackButton(onBackClick: () -> Unit) {
    IconButton(onClick = onBackClick) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = stringResource(id = R.string.quiz_back_content_description),
            tint = QuiziaTheme.colorScheme.onSurface,
        )
    }
}

private fun QuizCategory.toIcon(): ImageVector = when (this) {
    QuizCategory.GENERAL_KNOWLEDGE -> Icons.Rounded.EmojiObjects
    QuizCategory.HISTORY_AND_GEOGRAPHY -> Icons.Rounded.AccountBalance
    QuizCategory.INTERNATIONAL_MUSIC -> Icons.Rounded.MusicNote
    QuizCategory.MOVIES_AND_TV -> Icons.Rounded.Movie
    QuizCategory.SPORTS -> Icons.Rounded.SportsSoccer
    QuizCategory.ASTRONOMY -> Icons.Rounded.Public
    QuizCategory.NATURE -> Icons.Rounded.Eco
    QuizCategory.TECHNOLOGY -> Icons.Rounded.Code
    QuizCategory.GAMES -> Icons.Rounded.SportsEsports
    QuizCategory.CURRENT_EVENTS -> Icons.Rounded.Newspaper
}

private fun QuizCategory.labelResId(): Int = when (this) {
    QuizCategory.GENERAL_KNOWLEDGE -> R.string.category_general_knowledge
    QuizCategory.HISTORY_AND_GEOGRAPHY -> R.string.category_history_geography
    QuizCategory.INTERNATIONAL_MUSIC -> R.string.category_international_music
    QuizCategory.MOVIES_AND_TV -> R.string.category_movies_tv
    QuizCategory.SPORTS -> R.string.category_sports
    QuizCategory.ASTRONOMY -> R.string.category_astronomy
    QuizCategory.NATURE -> R.string.category_nature
    QuizCategory.TECHNOLOGY -> R.string.category_technology
    QuizCategory.GAMES -> R.string.category_games
    QuizCategory.CURRENT_EVENTS -> R.string.category_current_events
}

private fun QuizUiState.resolveOptionState(optionIndex: Int): OptionCardState = when {
    !answerRevealed -> OptionCardState.Default
    optionIndex == selectedOptionIndex && optionIndex == question?.correctIndex -> OptionCardState.Correct
    optionIndex == selectedOptionIndex -> OptionCardState.Wrong
    else -> OptionCardState.Default
}

private val PREVIEW_QUESTION = Question(
    text = "What is the capital of Japan?",
    options = listOf("Seoul", "Beijing", "Tokyo", "Bangkok"),
    correctIndex = 2,
    explanation = "Tokyo is the capital of Japan.",
)

@Preview(name = "Loading – Light", showBackground = true)
@Preview(name = "Loading – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuizScreenLoadingPreview() {
    QuiziaTheme {
        QuizScreen(
            uiState = QuizUiState(isLoading = true, totalQuestions = 5),
            category = QuizCategory.GENERAL_KNOWLEDGE,
            onOptionClick = {},
            onNextClick = {},
            onRetryClick = {},
            onBackClick = {},
        )
    }
}

@Preview(name = "Default – Light", showBackground = true)
@Preview(name = "Default – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuizScreenDefaultPreview() {
    QuiziaTheme {
        QuizScreen(
            uiState = QuizUiState(
                currentQuestionIndex = 2,
                totalQuestions = 10,
                question = PREVIEW_QUESTION,
            ),
            category = QuizCategory.GENERAL_KNOWLEDGE,
            onOptionClick = {},
            onNextClick = {},
            onRetryClick = {},
            onBackClick = {},
        )
    }
}

@Preview(name = "Correct – Light", showBackground = true)
@Preview(name = "Correct – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuizScreenCorrectPreview() {
    QuiziaTheme {
        QuizScreen(
            uiState = QuizUiState(
                currentQuestionIndex = 2,
                totalQuestions = 10,
                question = PREVIEW_QUESTION,
                selectedOptionIndex = 2,
                answerRevealed = true,
                correctAnswerText = "Tokyo",
            ),
            category = QuizCategory.GENERAL_KNOWLEDGE,
            onOptionClick = {},
            onNextClick = {},
            onRetryClick = {},
            onBackClick = {},
        )
    }
}

@Preview(name = "Wrong – Light", showBackground = true)
@Preview(name = "Wrong – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuizScreenWrongPreview() {
    QuiziaTheme {
        QuizScreen(
            uiState = QuizUiState(
                currentQuestionIndex = 2,
                totalQuestions = 10,
                question = PREVIEW_QUESTION,
                selectedOptionIndex = 3,
                answerRevealed = true,
                correctAnswerText = "Tokyo",
            ),
            category = QuizCategory.GENERAL_KNOWLEDGE,
            onOptionClick = {},
            onNextClick = {},
            onRetryClick = {},
            onBackClick = {},
        )
    }
}

@Preview(name = "Error – Light", showBackground = true)
@Preview(name = "Error – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuizScreenErrorPreview() {
    QuiziaTheme {
        QuizScreen(
            uiState = QuizUiState(errorMessageResId = R.string.quiz_error_missing_api_key),
            category = QuizCategory.TECHNOLOGY,
            onOptionClick = {},
            onNextClick = {},
            onRetryClick = {},
            onBackClick = {},
        )
    }
}
