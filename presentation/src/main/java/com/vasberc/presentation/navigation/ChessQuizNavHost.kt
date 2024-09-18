package com.vasberc.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vasberc.presentation.screens.LauncherScreen
import com.vasberc.presentation.uimodels.SnackbarMessage

@Composable
fun ChessQuizNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onMessage: (message: SnackbarMessage) -> Unit
) {

    NavHost(navController = navController,  startDestination = ChessQuizRoutes.LauncherScreen.route, modifier = modifier) {
        composable(ChessQuizRoutes.LauncherScreen.route) {
            LauncherScreen(navController = navController)
        }

        composable(ChessQuizRoutes.BoardScreen.route, ChessQuizRoutes.BoardScreen.arguments) {

        }
    }
}

sealed class ChessQuizRoutes(val route: String) {
    data object LauncherScreen : ChessQuizRoutes("LauncherScreen")
    data object BoardScreen : ChessQuizRoutes("BoardScreen?isResume={isResume}") {
        val arguments = listOf(
            navArgument("isResume") {
                type = NavType.BoolType
                defaultValue = false
            }
        )
    }
}