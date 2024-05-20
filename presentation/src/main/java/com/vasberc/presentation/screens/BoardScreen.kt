package com.vasberc.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.vasberc.presentation.R
import com.vasberc.presentation.componets.AnimateVisibilityY
import com.vasberc.presentation.componets.BackgroundComposable
import com.vasberc.presentation.componets.LoadingSpinnerWithMask
import com.vasberc.presentation.componets.UserOptionsPopUp
import com.vasberc.presentation.model.Board
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
        session = session,
        setSession = { viewModel.setSession(it) }
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
    setSession: (session: Session) -> Unit,
    onNavigateBack: () -> Unit
) {
    AnimateVisibilityY(isVisible = session == null) {
        UserOptionsPopUp {
            setSession(it)
        }
    }


    if(session != null) {
        val board by remember {
            mutableStateOf(
                Board(session)
            )
        }

        val boxes by remember {
            mutableStateOf(board.getBoxes())
        }

        val density = LocalDensity.current
        var boxSize by remember {
            mutableStateOf(25.dp)
        }

        val horseExists by remember(session) {
            derivedStateOf { boxes.any { it.getHasHorse() } }
        }

        val enableClick by remember(session) {
            derivedStateOf { !horseExists || boxes.none { it.getIsTarget() } }
        }

        LazyHorizontalGrid(
            modifier = Modifier
                .padding(0.dp, 25.dp)
                .fillMaxSize()
                .onGloballyPositioned {
                    with(density) {
                        boxSize = it.size.height.toDp() / board.getSize()
                    }
                }
            ,
            rows = GridCells.Fixed(board.getSize())
        ) {

            items(boxes.size) { index ->

                key(index) {
                    val item = boxes[index]
                    Row {
                        if(item.x == 0) {
                            Spacer(modifier = Modifier.width(25.dp))
                        }
                        Box(
                            modifier = Modifier
                                .size(boxSize)
                                .background(
                                    item.color
                                )
                                .clickable(enabled = enableClick && !item.getHasHorse()) {
                                    val newSession = if (horseExists) {
                                        item.setIsTarget(true)
                                        session.copy(
                                            targetBox = Pair(item.x, item.y)
                                        )
                                    } else {
                                        item.setHasHorse(true)
                                        session.copy(
                                            horseBox = Pair(item.x, item.y)
                                        )
                                    }
                                    setSession(newSession)
                                }
                        ) {
                            if(item.getHasHorse()) {
                                Image(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(id = R.drawable.ic_chess_horse),
                                    contentDescription = "Horse",
                                    contentScale = ContentScale.FillBounds
                                )
                            } else if (item.getIsTarget()) {
                                Image(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(id = R.drawable.ic_target),
                                    contentDescription = "Target",
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                        }
                        if(item.x == board.getSize().minus(1)) {
                            Spacer(modifier = Modifier.width(25.dp))
                        }
                    }

                }
            }
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
            BoardScreenContent(null, {}){}
        }
    }
}