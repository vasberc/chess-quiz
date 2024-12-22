package com.vasberc.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vasberc.presentation.screens.LauncherScreen
import com.vasberc.presentation.uimodels.SnackbarMessage
import kotlinx.serialization.Serializable

@Composable
fun ChessQuizNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onMessage: (message: SnackbarMessage) -> Unit
) {

    NavHost(navController = navController,  startDestination = ChessQuizRoutes.LauncherScreen, modifier = modifier) {
        composable<ChessQuizRoutes.LauncherScreen> {
            LauncherScreen(navController = navController)
        }

        composable<ChessQuizRoutes.BoardScreen> {

        }
    }
}


sealed class ChessQuizRoutes {
    @Serializable
    data object LauncherScreen
    @Serializable
    data class BoardScreen(val isResume: Boolean) : ChessQuizRoutes()
}