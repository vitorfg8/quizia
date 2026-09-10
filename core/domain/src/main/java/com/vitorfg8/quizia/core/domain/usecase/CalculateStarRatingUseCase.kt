package com.vitorfg8.quizia.core.domain.usecase

private const val PERCENT_SCALE = 100
private const val NO_STARS = 0

/** Minimum score percentage required for each star count, highest tier first. */
private val STAR_THRESHOLDS = listOf(
    100 to 5,
    80 to 4,
    60 to 3,
    40 to 2,
    1 to 1,
)

/** Turns a quiz score into the 0..5 star rating shown on the results screen. */
class CalculateStarRatingUseCase {

    operator fun invoke(score: Int, total: Int): Int {
        if (total <= 0) return NO_STARS
        val percentage = score.coerceIn(NO_STARS, total) * PERCENT_SCALE / total
        val tier = STAR_THRESHOLDS.firstOrNull { (floor, _) -> percentage >= floor }
        return tier?.second ?: NO_STARS
    }
}
