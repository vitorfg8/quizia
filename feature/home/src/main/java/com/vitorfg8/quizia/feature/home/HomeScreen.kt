package com.vitorfg8.quizia.feature.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.designsystem.QuiziaTheme
import com.vitorfg8.quizia.designsystem.icons.QuiziaIcons
import com.vitorfg8.quizia.designsystem.components.QuiziaTopBar
import com.vitorfg8.quizia.designsystem.components.QuiziaWordmark
import com.vitorfg8.quizia.feature.home.components.CategoryCard
import org.koin.androidx.compose.koinViewModel

private const val GRID_COLUMNS = 2

@Composable
fun HomeRoute(
    onCategoryClick: (QuizCategory) -> Unit,
    onSettingsClick: () -> Unit,
    onMissingProvider: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.ensureProviderAvailable()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is HomeSideEffect.NavigateToQuiz -> onCategoryClick(effect.category)
                HomeSideEffect.NavigateToSettings -> onSettingsClick()
                HomeSideEffect.NavigateToWelcome -> onMissingProvider()
            }
        }
    }
    HomeScreen(
        uiState = uiState,
        onCategoryClick = viewModel::onCategoryClick,
        onSettingsClick = viewModel::onSettingsClick,
        modifier = modifier,
    )
}

@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    onCategoryClick: (QuizCategory) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = QuiziaTheme.colorScheme.background,
        topBar = {
            QuiziaTopBar(
                title = { QuiziaWordmark() },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = QuiziaIcons.Settings,
                            contentDescription = stringResource(id = R.string.home_settings_content_description),
                            tint = QuiziaTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(count = GRID_COLUMNS),
            contentPadding = PaddingValues(
                start = QuiziaTheme.spacing.large,
                end = QuiziaTheme.spacing.large,
                bottom = QuiziaTheme.spacing.large,
            ),
            verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.medium),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                HomeGreeting()
            }
            items(uiState.categories) { category ->
                val uiModel = category.toUiModel()
                CategoryCard(
                    icon = uiModel.icon,
                    label = stringResource(id = uiModel.labelResId),
                    iconTint = uiModel.accent.toColor(),
                    onClick = { onCategoryClick(category) },
                )
            }
        }
    }
}

@Composable
private fun HomeGreeting(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = QuiziaTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.small),
    ) {
        Text(
            text = stringResource(id = R.string.home_greeting_title),
            style = QuiziaTheme.typography.headlineMedium,
            color = QuiziaTheme.colorScheme.onBackground,
        )
        Text(
            text = stringResource(id = R.string.home_greeting_subtitle),
            style = QuiziaTheme.typography.bodyLarge,
            color = QuiziaTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** Accents that keep the category grid varied without leaving the brand palette. */
private enum class CategoryAccent {
    Violet,
    Indigo,
    Orange,
    Green,
    Rose,
    Teal,
    Amber,
    Orchid,
}

@Composable
private fun CategoryAccent.toColor(): Color = when (this) {
    CategoryAccent.Violet -> QuiziaTheme.extendedColors.categoryViolet
    CategoryAccent.Indigo -> QuiziaTheme.extendedColors.categoryIndigo
    CategoryAccent.Orange -> QuiziaTheme.extendedColors.categoryOrange
    CategoryAccent.Green -> QuiziaTheme.extendedColors.categoryGreen
    CategoryAccent.Rose -> QuiziaTheme.extendedColors.categoryRose
    CategoryAccent.Teal -> QuiziaTheme.extendedColors.categoryTeal
    CategoryAccent.Amber -> QuiziaTheme.extendedColors.categoryAmber
    CategoryAccent.Orchid -> QuiziaTheme.extendedColors.categoryOrchid
}

private data class CategoryUiModel(
    val icon: ImageVector,
    @StringRes val labelResId: Int,
    val accent: CategoryAccent,
)

private fun QuizCategory.toUiModel(): CategoryUiModel = when (this) {
    QuizCategory.GENERAL_KNOWLEDGE -> CategoryUiModel(
        icon = QuiziaIcons.Lightbulb,
        labelResId = R.string.category_general_knowledge,
        accent = CategoryAccent.Amber,
    )
    QuizCategory.HISTORY_AND_GEOGRAPHY -> CategoryUiModel(
        icon = QuiziaIcons.Bank,
        labelResId = R.string.category_history_geography,
        accent = CategoryAccent.Violet,
    )
    QuizCategory.INTERNATIONAL_MUSIC -> CategoryUiModel(
        icon = QuiziaIcons.MusicNote,
        labelResId = R.string.category_international_music,
        accent = CategoryAccent.Rose,
    )
    QuizCategory.MOVIES_AND_TV -> CategoryUiModel(
        icon = QuiziaIcons.FilmSlate,
        labelResId = R.string.category_movies_tv,
        accent = CategoryAccent.Orchid,
    )
    QuizCategory.SPORTS -> CategoryUiModel(
        icon = QuiziaIcons.SoccerBall,
        labelResId = R.string.category_sports,
        accent = CategoryAccent.Orange,
    )
    QuizCategory.ASTRONOMY -> CategoryUiModel(
        icon = QuiziaIcons.Planet,
        labelResId = R.string.category_astronomy,
        accent = CategoryAccent.Indigo,
    )
    QuizCategory.NATURE -> CategoryUiModel(
        icon = QuiziaIcons.Leaf,
        labelResId = R.string.category_nature,
        accent = CategoryAccent.Green,
    )
    QuizCategory.TECHNOLOGY -> CategoryUiModel(
        icon = QuiziaIcons.Code,
        labelResId = R.string.category_technology,
        accent = CategoryAccent.Teal,
    )
    QuizCategory.GAMES -> CategoryUiModel(
        icon = QuiziaIcons.GameController,
        labelResId = R.string.category_games,
        accent = CategoryAccent.Rose,
    )
    QuizCategory.CURRENT_EVENTS -> CategoryUiModel(
        icon = QuiziaIcons.Newspaper,
        labelResId = R.string.category_current_events,
        accent = CategoryAccent.Indigo,
    )
}

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenPreview() {
    QuiziaTheme {
        HomeScreen(
            uiState = HomeUiState(),
            onCategoryClick = {},
            onSettingsClick = {},
        )
    }
}
