package com.vitorfg8.quizia.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vitorfg8.quizia.core.domain.model.QuizCategory
import com.vitorfg8.quizia.feature.home.HomeRoute
import com.vitorfg8.quizia.feature.llmsetup.LlmSetupRoute
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
                onCategoryClick = { _: QuizCategory -> /* TODO: navigate to quiz */ },
                onSettingsClick = { /* TODO: navigate to settings */ },
            )
        }
    }
}
