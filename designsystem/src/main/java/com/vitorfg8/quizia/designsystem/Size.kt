package com.vitorfg8.quizia.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Fixed component dimensions of the Quizia design system.
 * Every value is a multiple of 4.dp so components stay aligned to the same grid as
 * [QuiziaSpacing]. Access them through `QuiziaTheme.sizes`.
 */
@Immutable
data class QuiziaSizes(
    val iconSmall: Dp = 16.dp,
    val iconMedium: Dp = 24.dp,
    val iconLarge: Dp = 40.dp,
    val iconHuge: Dp = 96.dp,
    val brandMark: Dp = 80.dp,
    val buttonHeight: Dp = 52.dp,
    val chipHeight: Dp = 44.dp,
    val categoryChipHeight: Dp = 32.dp,
    val minTouchTarget: Dp = 48.dp,
    val optionCardMinHeight: Dp = 64.dp,
    val categoryCardHeight: Dp = 136.dp,
    val resultHeroSize: Dp = 112.dp,
    val progressBarHeight: Dp = 4.dp,
    val radioDot: Dp = 8.dp,
    val borderHairline: Dp = 1.dp,
    val borderSelected: Dp = 4.dp,
    val elevationNone: Dp = 0.dp,
    val elevationRaised: Dp = 4.dp,
)

internal val LocalQuiziaSizes = staticCompositionLocalOf { QuiziaSizes() }
