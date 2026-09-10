package com.vitorfg8.quizia.feature.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.EmojiObjects
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SportsSoccer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.designsystem.QuiziaTheme
import com.vitorfg8.quizia.designsystem.components.QuiziaTopBar
import com.vitorfg8.quizia.feature.home.components.CategoryCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeRoute(
    onCategoryClick: (QuizCategory) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is HomeSideEffect.NavigateToQuiz -> onCategoryClick(effect.category)
                HomeSideEffect.NavigateToSettings -> onSettingsClick()
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

@OptIn(ExperimentalMaterial3Api::class)
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
                title = stringResource(id = R.string.home_title),
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = stringResource(id = R.string.home_settings_content_description),
                            tint = QuiziaTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 2),
            contentPadding = PaddingValues(QuiziaTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(QuiziaTheme.spacing.medium),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            items(uiState.categories) { category ->
                val uiModel = category.toUiModel()
                CategoryCard(
                    icon = uiModel.icon,
                    label = stringResource(id = uiModel.labelResId),
                    onClick = { onCategoryClick(category) },
                )
            }
        }
    }
}

private data class CategoryUiModel(
    val icon: ImageVector,
    @StringRes val labelResId: Int,
)

private fun QuizCategory.toUiModel(): CategoryUiModel = when (this) {
    QuizCategory.GENERAL_KNOWLEDGE -> CategoryUiModel(
        icon = Icons.Rounded.EmojiObjects,
        labelResId = R.string.category_general_knowledge,
    )
    QuizCategory.HISTORY_AND_GEOGRAPHY -> CategoryUiModel(
        icon = Icons.Rounded.Public,
        labelResId = R.string.category_history_geography,
    )
    QuizCategory.INTERNATIONAL_MUSIC -> CategoryUiModel(
        icon = Icons.Rounded.MusicNote,
        labelResId = R.string.category_international_music,
    )
    QuizCategory.MOVIES_AND_TV -> CategoryUiModel(
        icon = Icons.Rounded.Movie,
        labelResId = R.string.category_movies_tv,
    )
    QuizCategory.SPORTS -> CategoryUiModel(
        icon = Icons.Rounded.SportsSoccer,
        labelResId = R.string.category_sports,
    )
    QuizCategory.ASTRONOMY -> CategoryUiModel(
        icon = Icons.Rounded.AutoAwesome,
        labelResId = R.string.category_astronomy,
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
