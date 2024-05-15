package com.vasberc.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.vasberc.presentation.ui.theme.ChessQuizTheme
import com.vasberc.presentation.viewmodels.LauncherScreenViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun LauncherScreen(
    navController: NavHostController,
    viewModel: LauncherScreenViewModel = getViewModel()
) {
    LauncherScreenContent()
}

@Composable
fun LauncherScreenContent() {

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LauncherScreenPreview() {
    ChessQuizTheme {
        LauncherScreenContent()
    }
}

