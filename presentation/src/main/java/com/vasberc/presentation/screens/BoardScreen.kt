package com.vasberc.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.vasberc.presentation.R
import com.vasberc.presentation.componets.BackgroundComposable
import com.vasberc.presentation.componets.LoadingSpinnerWithMask
import com.vasberc.presentation.componets.UserOptionsPopUp
import com.vasberc.presentation.model.ScreenState
import com.vasberc.presentation.model.Session
import com.vasberc.presentation.viewmodels.BoardScreenViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun BoardScreen(
    navController: NavHostController,
    onShowMessage: (message: String) -> Unit,
    viewModel: BoardScreenViewModel = getViewModel()
) {

    val isLoading by remember {
        derivedStateOf { viewModel.screenState is ScreenState.Loading }
    }
    
    val session by remember {
        derivedStateOf { 
            if(viewModel.screenState is ScreenState.Success) {
                (viewModel.screenState as ScreenState.Success<BoardScreenViewModel.BoardScreenState>).data.session
            } else {
                null
            }
        }
    }

    //Prevent back press while loading
    BackHandler(enabled = isLoading) { }

    BoardScreenContent(
        session
    ) {
        navController.popBackStack()
    }

    if(isLoading) {
        LoadingSpinnerWithMask()
    }
}

@Composable
fun BoardScreenContent(
    session: Session?,
    onNavigateBack: () -> Unit
) {
    AnimatedVisibility(
        enter = slideInVertically(
            initialOffsetY = {
                //To start out of the screen the offset is set to the height of the component +
                //the padding value
                it
            },
        ),
        exit = slideOutVertically(
            targetOffsetY = {
                //Same with initial starting logic to go out of the screen
                it
            },
        ),
        visible = session == null
    ) {
        UserOptionsPopUp {

        }
    }
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
            BoardScreenContent(null){}
        }
    }
}