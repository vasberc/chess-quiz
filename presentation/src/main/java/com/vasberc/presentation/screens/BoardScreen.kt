package com.vasberc.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.vasberc.presentation.R
import com.vasberc.presentation.componets.BackgroundComposable
import com.vasberc.presentation.viewmodels.BoardScreenViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun BoardScreen(
    navController: NavHostController,
    onShowMessage: (message: String) -> Unit,
    viewModel: BoardScreenViewModel = getViewModel()
) {
    BoardScreenContent {
        navController.popBackStack()
    }
}

@Composable
fun BoardScreenContent(
    onNavigateBack: () -> Unit
) {

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BoardScreenPreview() {
    Surface(
        modifier = Modifier
            .paint(
                painter = painterResource(id = R.drawable.ic_chess),
                contentScale = ContentScale.FillBounds
            )
            .fillMaxSize()

    ) {
        BackgroundComposable {
            BoardScreenContent{}
        }
    }
}