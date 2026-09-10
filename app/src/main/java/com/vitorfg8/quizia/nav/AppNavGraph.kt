package com.vitorfg8.quizia.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.feature.home.HomeRoute
import com.vitorfg8.quizia.feature.llmsetup.LlmSetupRoute
import com.vitorfg8.quizia.feature.quiz.QuizRoute
import com.vitorfg8.quizia.feature.results.ResultsRoute
import com.vitorfg8.quizia.feature.welcome.WelcomeScreen

@Composable
internal fun AppNavGraph(
    isFirstRun: Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val startDestination = if (isFirstRun) AppRoute.WELCOME else AppRoute.HOME

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(AppRoute.WELCOME) {
            WelcomeScreen(
                onContinueClick = { navController.navigate(AppRoute.LLM_SETUP) },
            )
        }

        composable(AppRoute.LLM_SETUP) {
            LlmSetupRoute(
                onSetupComplete = {
                    navController.navigate(AppRoute.HOME) {
                        popUpTo(AppRoute.WELCOME) { inclusive = true }
                    }
                },
            )
        }

        composable(AppRoute.HOME) {
            HomeRoute(
                onCategoryClick = { category ->
                    navController.navigate(AppRoute.buildQuizRoute(category.name))
                },
                onSettingsClick = { /* The settings screen is not implemented yet. */ },
            )
        }

        composable(
            route = AppRoute.QUIZ,
            arguments = listOf(navArgument(AppRoute.ARG_CATEGORY) { type = NavType.StringType }),
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString(AppRoute.ARG_CATEGORY)
            QuizRoute(
                category = categoryName.toQuizCategory(),
                onQuizFinished = { score, total ->
                    navController.navigate(AppRoute.buildResultsRoute(score, total)) {
                        popUpTo(AppRoute.HOME)
                    }
                },
            )
        }

        composable(
            route = AppRoute.RESULTS,
            arguments = listOf(
                navArgument(AppRoute.ARG_SCORE) { type = NavType.IntType },
                navArgument(AppRoute.ARG_TOTAL) { type = NavType.IntType },
            ),
        ) { backStackEntry ->
            val arguments = backStackEntry.arguments
            ResultsRoute(
                score = arguments?.getInt(AppRoute.ARG_SCORE) ?: 0,
                total = arguments?.getInt(AppRoute.ARG_TOTAL) ?: 0,
                onBackToHome = {
                    navController.navigate(AppRoute.HOME) {
                        popUpTo(AppRoute.HOME) { inclusive = true }
                    }
                },
            )
        }
    }
}

internal fun String?.toQuizCategory(): QuizCategory = this
    ?.let { name -> runCatching { QuizCategory.valueOf(name) }.getOrNull() }
    ?: QuizCategory.GENERAL_KNOWLEDGE
