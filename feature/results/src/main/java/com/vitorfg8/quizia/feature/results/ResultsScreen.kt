package com.vitorfg8.quizia.feature.results

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitorfg8.quizia.designsystem.QuiziaTheme
import com.vitorfg8.quizia.designsystem.components.QuiziaButton
import com.vitorfg8.quizia.designsystem.components.QuiziaOutlinedButton
import com.vitorfg8.quizia.designsystem.components.QuiziaScoreIndicator
import com.vitorfg8.quizia.designsystem.components.QuiziaStatRow
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Spread
import nl.dionsegijn.konfetti.core.emitter.Emitter
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.util.concurrent.TimeUnit

private const val MILLIS_PER_SECOND = 1_000
private const val SECONDS_PER_MINUTE = 60
private const val CONFETTI_MIN_SPEED = 0f
private const val CONFETTI_MAX_SPEED = 15f
private const val CONFETTI_DAMPING = 0.9f
private const val CONFETTI_EMIT_SECONDS = 5L
private const val CONFETTI_PER_SECOND = 100
private const val CONFETTI_ORIGIN_START = 0.0
private const val CONFETTI_ORIGIN_END = 1.0

@Composable
fun ResultsRoute(
    score: Int,
    total: Int,
    elapsedMs: Long,
    onPlayAnother: () -> Unit,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ResultsViewModel = koinViewModel { parametersOf(score, total, elapsedMs) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                ResultsSideEffect.NavigateToQuiz -> onPlayAnother()
                ResultsSideEffect.NavigateToHome -> onBackToHome()
            }
        }
    }
    ResultsScreen(
        uiState = uiState,
        onPlayAnotherClick = viewModel::onPlayAnotherClick,
        onBackToHomeClick = viewModel::onBackToHomeClick,
        modifier = modifier,
    )
}

@Composable
internal fun ResultsScreen(
    uiState: ResultsUiState,
    onPlayAnotherClick: () -> Unit,
    onBackToHomeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        ResultsScaffold(
            uiState = uiState,
            onPlayAnotherClick = onPlayAnotherClick,
            onBackToHomeClick = onBackToHomeClick,
        )
        if (uiState.shouldCelebrate) {
            ResultsCelebration()
        }
    }
}

@Composable
private fun ResultsScaffold(
    uiState: ResultsUiState,
    onPlayAnotherClick: () -> Unit,
    onBackToHomeClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = QuiziaTheme.colorScheme.background,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(QuiziaTheme.spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ResultsBackButton(onBackClick = onBackToHomeClick)
            Spacer(modifier = Modifier.weight(1f))
            ResultsSummary(uiState = uiState)
            Spacer(modifier = Modifier.weight(1f))
            ResultsActions(
                onPlayAnotherClick = onPlayAnotherClick,
                onBackToHomeClick = onBackToHomeClick,
            )
        }
    }
}

@Composable
private fun ResultsCelebration() {
    val parties: List<Party> = rememberCelebrationParties()
    if (!LocalInspectionMode.current) {
        KonfettiView(
            modifier = Modifier.fillMaxSize(),
            parties = parties,
        )
    }
}

@Composable
private fun rememberCelebrationParties(): List<Party> {
    val colors: List<Int> = listOf(
        QuiziaTheme.extendedColors.success.toArgb(),
        QuiziaTheme.extendedColors.categoryGreen.toArgb(),
        QuiziaTheme.colorScheme.primary.toArgb(),
        QuiziaTheme.extendedColors.categoryViolet.toArgb(),
    )
    return remember(colors) { celebrationRain(colors) }
}

private fun celebrationRain(colors: List<Int>): List<Party> = listOf(
    Party(
        speed = CONFETTI_MIN_SPEED,
        maxSpeed = CONFETTI_MAX_SPEED,
        damping = CONFETTI_DAMPING,
        angle = Angle.BOTTOM,
        spread = Spread.ROUND,
        colors = colors,
        emitter = Emitter(duration = CONFETTI_EMIT_SECONDS, TimeUnit.SECONDS)
            .perSecond(CONFETTI_PER_SECOND),
        position = Position.Relative(CONFETTI_ORIGIN_START, CONFETTI_ORIGIN_START)
            .between(Position.Relative(CONFETTI_ORIGIN_END, CONFETTI_ORIGIN_START)),
    ),
)

@Composable
private fun ResultsBackButton(onBackClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(id = R.string.results_back_content_description),
                tint = QuiziaTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun ResultsSummary(
    uiState: ResultsUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.medium),
    ) {
        QuiziaScoreIndicator(
            progress = uiState.scoreProgress(),
            label = stringResource(
                id = R.string.results_score_fraction,
                uiState.score,
                uiState.total,
            ),
            isSuccess = uiState.isSuccess,
        )
        Text(
            text = stringResource(id = R.string.results_title),
            style = QuiziaTheme.typography.headlineMedium,
            color = QuiziaTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(
                id = R.string.results_subtitle,
                uiState.score,
                uiState.total,
            ),
            style = QuiziaTheme.typography.bodyLarge,
            color = QuiziaTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        ResultsStatsCard(uiState = uiState)
    }
}

@Composable
private fun ResultsStatsCard(
    uiState: ResultsUiState,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = QuiziaTheme.shapes.large,
        color = QuiziaTheme.colorScheme.surface,
        border = BorderStroke(
            width = QuiziaTheme.sizes.borderHairline,
            color = QuiziaTheme.colorScheme.outline,
        ),
    ) {
        Column(modifier = Modifier.padding(QuiziaTheme.spacing.large)) {
            QuiziaStatRow(
                icon = Icons.Rounded.CheckCircle,
                label = stringResource(id = R.string.results_stat_correct),
                value = uiState.score.toString(),
                iconTint = QuiziaTheme.extendedColors.success,
            )
            ResultsStatsDivider()
            QuiziaStatRow(
                icon = Icons.Rounded.Cancel,
                label = stringResource(id = R.string.results_stat_wrong),
                value = uiState.wrongCount.toString(),
                iconTint = QuiziaTheme.extendedColors.error,
            )
            ResultsStatsDivider()
            QuiziaStatRow(
                icon = Icons.Rounded.AccessTime,
                label = stringResource(id = R.string.results_stat_time),
                value = formatElapsedDuration(uiState.elapsedMs),
                iconTint = QuiziaTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun ResultsStatsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = QuiziaTheme.spacing.medium),
        thickness = QuiziaTheme.sizes.borderHairline,
        color = QuiziaTheme.colorScheme.outline,
    )
}

@Composable
private fun ResultsActions(
    onPlayAnotherClick: () -> Unit,
    onBackToHomeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.small),
    ) {
        QuiziaButton(
            text = stringResource(id = R.string.results_play_another),
            onClick = onPlayAnotherClick,
            leadingIcon = Icons.Rounded.Refresh,
            modifier = Modifier.fillMaxWidth(),
        )
        QuiziaOutlinedButton(
            text = stringResource(id = R.string.results_back_to_home),
            onClick = onBackToHomeClick,
            leadingIcon = Icons.Rounded.Home,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun formatElapsedDuration(elapsedMs: Long): String {
    val totalSeconds = (elapsedMs / MILLIS_PER_SECOND).coerceAtLeast(0L)
    val minutes = totalSeconds / SECONDS_PER_MINUTE
    val seconds = totalSeconds % SECONDS_PER_MINUTE
    return stringResource(id = R.string.results_duration, minutes, seconds)
}

private fun ResultsUiState.scoreProgress(): Float {
    if (total == 0) return 0f
    return score.toFloat() / total
}

private const val PREVIEW_TOTAL = 10
private const val PREVIEW_SUCCESS_ELAPSED_MS = 392_000L
private const val PREVIEW_FAILURE_ELAPSED_MS = 318_000L

@Preview(name = "Success – Light", showBackground = true)
@Preview(name = "Success – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ResultsScreenSuccessPreview() {
    QuiziaTheme {
        ResultsScreen(
            uiState = ResultsUiState(
                score = 8,
                total = PREVIEW_TOTAL,
                elapsedMs = PREVIEW_SUCCESS_ELAPSED_MS,
            ),
            onPlayAnotherClick = {},
            onBackToHomeClick = {},
        )
    }
}

@Preview(name = "Failure – Light", showBackground = true)
@Preview(name = "Failure – Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ResultsScreenFailurePreview() {
    QuiziaTheme {
        ResultsScreen(
            uiState = ResultsUiState(
                score = 4,
                total = PREVIEW_TOTAL,
                elapsedMs = PREVIEW_FAILURE_ELAPSED_MS,
            ),
            onPlayAnotherClick = {},
            onBackToHomeClick = {},
        )
    }
}
