package com.vitorfg8.quizia.feature.results

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitorfg8.quizia.designsystem.QuiziaTheme
import com.vitorfg8.quizia.designsystem.components.QUIZIA_MAX_STARS
import com.vitorfg8.quizia.designsystem.components.QuiziaButton
import com.vitorfg8.quizia.designsystem.components.QuiziaStarRating
import com.vitorfg8.quizia.designsystem.components.QuiziaTopBar
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ResultsRoute(
    score: Int,
    total: Int,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ResultsViewModel = koinViewModel { parametersOf(score, total) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                ResultsSideEffect.NavigateToHome -> onBackToHome()
            }
        }
    }
    ResultsScreen(
        uiState = uiState,
        onBackToHomeClick = viewModel::onBackToHomeClick,
        modifier = modifier,
    )
}

@Composable
internal fun ResultsScreen(
    uiState: ResultsUiState,
    onBackToHomeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = QuiziaTheme.colorScheme.background,
        topBar = { QuiziaTopBar(title = stringResource(id = R.string.results_title)) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(QuiziaTheme.spacing.large),
            verticalArrangement = Arrangement.spacedBy(
                space = QuiziaTheme.spacing.large,
                alignment = Alignment.CenterVertically,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            QuiziaStarRating(
                filledStars = uiState.stars,
                contentDescription = stringResource(
                    id = R.string.results_stars_content_description,
                    uiState.stars,
                    QUIZIA_MAX_STARS,
                ),
            )
            Text(
                text = stringResource(id = R.string.results_score, uiState.score, uiState.total),
                style = QuiziaTheme.typography.headlineSmall,
                color = QuiziaTheme.colorScheme.onBackground,
            )
            Text(
                text = stringResource(id = uiState.performanceMessageResId),
                style = QuiziaTheme.typography.bodyLarge,
                color = QuiziaTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            QuiziaButton(
                text = stringResource(id = R.string.results_back_to_home),
                onClick = onBackToHomeClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview(name = "No stars – Light", showBackground = true)
@Preview(name = "No stars – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ResultsScreenZeroStarsPreview() {
    ResultsScreenPreview(score = 0, stars = 0, messageResId = R.string.results_message_zero_stars)
}

@Preview(name = "Three stars – Light", showBackground = true)
@Preview(name = "Three stars – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ResultsScreenThreeStarsPreview() {
    ResultsScreenPreview(score = 3, stars = 3, messageResId = R.string.results_message_three_stars)
}

@Preview(name = "Five stars – Light", showBackground = true)
@Preview(name = "Five stars – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ResultsScreenFiveStarsPreview() {
    ResultsScreenPreview(score = 5, stars = 5, messageResId = R.string.results_message_five_stars)
}

@Composable
private fun ResultsScreenPreview(score: Int, stars: Int, messageResId: Int) {
    QuiziaTheme {
        ResultsScreen(
            uiState = ResultsUiState(
                score = score,
                total = QUIZIA_MAX_STARS,
                stars = stars,
                performanceMessageResId = messageResId,
            ),
            onBackToHomeClick = {},
        )
    }
}
