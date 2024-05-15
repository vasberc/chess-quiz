package com.vasberc.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun ChessQuizNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onMessage: (message: String) -> Unit
) {

    NavHost(navController = navController,  startDestination = ChessQuizRoutes.LauncherScreen.route, modifier = modifier) {
        composable(ChessQuizRoutes.LauncherScreen.route) {

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