package com.vasberc.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.vasberc.presentation.R
import com.vasberc.presentation.componets.BackgroundComposable
import com.vasberc.presentation.navigation.ChessQuizRoutes
import com.vasberc.presentation.viewmodels.LauncherScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LauncherScreen(
    navController: NavHostController,
    viewModel: LauncherScreenViewModel = koinViewModel()
) {
    LauncherScreenContent(isResumeEnabled = false) { resume: Boolean ->
        navController.navigate(
            ChessQuizRoutes.BoardScreen(resume)
        )
    }
}

@Composable
fun LauncherScreenContent(
    isResumeEnabled: Boolean,
    onBoardScreen: (resume: Boolean) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(20.dp)
            .background(Color.White.copy(alpha = 0.8f))
            .fillMaxSize(),
    ) {
        val (text, newButton, resumeButton) = createRefs()
        Text(
            text = "Welcome to chess quiz",
            style = TextStyle(fontSize = 30.sp, textAlign = TextAlign.Center),
            modifier = Modifier.constrainAs(text) {
                top.linkTo(parent.top)
                bottom.linkTo(newButton.top)
                centerHorizontallyTo(parent)
            }
        )

        Button(
            modifier = Modifier.constrainAs(newButton) {
                top.linkTo(text.bottom)
                bottom.linkTo(resumeButton.top)
                centerHorizontallyTo(parent)
            },
            onClick = { onBoardScreen(false) }
        ) {
            Text(
                text = stringResource(R.string.start_new_game),
                style = TextStyle(fontSize = 14.sp)
            )
        }

        Button(
            modifier = Modifier.constrainAs(resumeButton) {
                top.linkTo(newButton.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(newButton.start)
                end.linkTo(newButton.end)
                width = Dimension.fillToConstraints
            },
            onClick = { onBoardScreen(false) },
            enabled = isResumeEnabled
        ) {
            Text(
                text = stringResource(R.string.resume),
                style = TextStyle(fontSize = 14.sp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LauncherScreenPreview() {
    Surface(
        modifier = Modifier
            .paint(
                painter = painterResource(id = R.drawable.ic_chess),
                contentScale = ContentScale.FillBounds
            )
            .fillMaxSize()

    ) {
        BackgroundComposable {
            LauncherScreenContent(isResumeEnabled = false) {}
        }
    }
}

