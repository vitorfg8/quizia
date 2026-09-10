package com.vitorfg8.quizia.feature.quiz

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.vitorfg8.quizia.designsystem.components.QuiziaButton
import com.vitorfg8.quizia.designsystem.components.QuiziaOptionCard
import com.vitorfg8.quizia.designsystem.components.QuiziaProgressBar
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun QuizRoute(
    category: QuizCategory,
    onQuizFinished: (score: Int, total: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuizViewModel = koinViewModel { parametersOf(category) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is QuizSideEffect.NavigateToResults -> onQuizFinished(effect.score, effect.total)
            }
        }
    }
    QuizScreen(
        uiState = uiState,
        category = category,
        onOptionClick = viewModel::selectOption,
        onNextClick = viewModel::goToNextQuestion,
        onRetryClick = viewModel::loadQuiz,
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
                .padding(QuiziaTheme.spacing.large),
            verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.medium),
        ) {
            when {
                uiState.isLoading -> LoadingContent()
                uiState.errorMessageResId != null -> ErrorContent(
                    messageResId = uiState.errorMessageResId,
                    onRetryClick = onRetryClick,
                )
                uiState.question != null -> QuestionContent(
                    uiState = uiState,
                    category = category,
                    question = uiState.question,
                    onOptionClick = onOptionClick,
                    onNextClick = onNextClick,
                )
            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
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

@Composable
private fun ErrorContent(
    @StringRes messageResId: Int,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
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

@Composable
private fun QuestionContent(
    uiState: QuizUiState,
    category: QuizCategory,
    question: Question,
    onOptionClick: (Int) -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.large),
    ) {
        QuizProgressHeader(uiState = uiState)
        Text(
            text = stringResource(id = category.labelResId()),
            style = QuiziaTheme.typography.labelLarge,
            color = QuiziaTheme.colorScheme.primary,
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
        if (uiState.answerRevealed && uiState.selectedOptionIndex != question.correctIndex) {
            Text(
                text = stringResource(id = R.string.quiz_correct_answer, uiState.correctAnswerText),
                style = QuiziaTheme.typography.bodyMedium,
                color = QuiziaTheme.colorScheme.onSurfaceVariant,
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
    modifier: Modifier = Modifier,
) {
    val progressDescription = stringResource(
        id = R.string.quiz_progress_content_description,
        uiState.currentQuestionIndex + 1,
        uiState.totalQuestions,
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = progressDescription },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.medium),
    ) {
        QuiziaProgressBar(
            progress = uiState.progress,
            modifier = Modifier.weight(1f),
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
    optionIndex == question?.correctIndex -> OptionCardState.Correct
    optionIndex == selectedOptionIndex -> OptionCardState.Wrong
    else -> OptionCardState.Default
}

@Preview(name = "Loading – Light", showBackground = true)
@Preview(name = "Loading – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuizScreenLoadingPreview() {
    QuiziaTheme {
        QuizScreen(
            uiState = QuizUiState(isLoading = true, totalQuestions = 5),
            category = QuizCategory.TECHNOLOGY,
            onOptionClick = {},
            onNextClick = {},
            onRetryClick = {},
        )
    }
}

@Preview(name = "Revealed – Light", showBackground = true)
@Preview(name = "Revealed – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun QuizScreenRevealedPreview() {
    QuiziaTheme {
        QuizScreen(
            uiState = QuizUiState(
                currentQuestionIndex = 2,
                totalQuestions = 8,
                question = Question(
                    text = "Which planet is the largest in the Solar System?",
                    options = listOf("Mars", "Jupiter", "Venus", "Mercury"),
                    correctIndex = 1,
                ),
                selectedOptionIndex = 2,
                answerRevealed = true,
                correctAnswerText = "Jupiter",
            ),
            category = QuizCategory.TECHNOLOGY,
            onOptionClick = {},
            onNextClick = {},
            onRetryClick = {},
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
        )
    }
}
