package com.vasberc.presentation.screens

import android.content.ClipData
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.vasberc.presentation.R
import com.vasberc.presentation.componets.BackgroundComposable
import com.vasberc.presentation.componets.BottomSheet
import com.vasberc.presentation.componets.clickableWithColor
import com.vasberc.presentation.componets.dragAndDropTargetElement
import com.vasberc.presentation.uimodels.Board
import com.vasberc.presentation.uimodels.Box
import com.vasberc.presentation.uimodels.SessionConfig
import com.vasberc.presentation.uimodels.SnackbarMessage
import com.vasberc.presentation.viewmodels.BoardScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun BoardScreen(
    navController: NavHostController,
    onMessage: (message: SnackbarMessage) -> Unit,
    viewModel: BoardScreenViewModel = koinViewModel()
) {

    val sessionConfig = viewModel.sessionConfig.collectAsStateWithLifecycle().value
    val calculating = viewModel.calculating.collectAsStateWithLifecycle().value

    BoardScreenContent(
        sessionConfig = sessionConfig,
        calculating = calculating,
        onBoxClicked = viewModel::onBoxClicked,
        onItemDropped = viewModel::onItemDropped,
        onConfigComplete = viewModel::setSessionConfig,
        onCalculateClicked = viewModel::onCalculateClicked
    )
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun BoardScreenContent(
    sessionConfig: SessionConfig?,
    calculating: Boolean,
    onBoxClicked: (index: Int) -> Unit,
    onItemDropped: (index: Int, isHorse: Boolean) -> Unit,
    onConfigComplete: (sessionConfig: SessionConfig) -> Unit,
    onCalculateClicked: () -> Unit
) {
    if (sessionConfig == null) {
        BottomSheet(onConfigComplete)
    } else {
        ConstraintLayout(
            modifier = Modifier
                .padding(20.dp)
                .background(Color.White.copy(alpha = 0.8f))
                .fillMaxSize()
        ) {
            val (board, calcButton) = createRefs()
            val verticalScrollState = rememberScrollState()
            val horizontalScrollState = rememberScrollState()

            ContextualFlowRow(
                itemCount = sessionConfig.board.boxes.size,
                maxItemsInEachRow = sessionConfig.board.boardSize,
                maxLines = sessionConfig.board.boardSize,
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .constrainAs(board) {
                        top.linkTo(parent.top)
                        bottom.linkTo(calcButton.top, 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = androidx.constraintlayout.compose.Dimension.fillToConstraints
                        height = androidx.constraintlayout.compose.Dimension.fillToConstraints
                    }
                    .verticalScroll(verticalScrollState)
                    .horizontalScroll(horizontalScrollState)
            ) { index ->

                var draggableBackground by remember {
                    mutableStateOf<Color?>(null)
                }

                val box = sessionConfig.board.boxes[index]
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            draggableBackground ?: if (box.isDark) Color.Black else Color.White
                        )
                        .clickableWithColor(
                            color = if (box.isDark) Color.White else Color.Black,
                            enabled =
                            (sessionConfig.board.target == null || sessionConfig.board.horse == null) &&
                                    sessionConfig.board.horse?.position != box && sessionConfig.board.target != box
                        ) {
                            onBoxClicked(index)
                        }
                        .padding(10.dp)
                        .dragAndDropTargetElement(
                            shouldStartDragAndDrop = { event ->
                                sessionConfig.board.target != box && sessionConfig.board.horse?.position != box
                            },
                            index = index,
                            onBackgroundChange = { color ->
                                draggableBackground = color
                            },
                            onItemDropped = { index, isHorse ->
                                onItemDropped(index, isHorse)
                            },
                            sessionConfig = sessionConfig
                        )
                ) {
                    if (sessionConfig.board.horse?.position == box) {
                        Image(
                            painter = painterResource(R.drawable.ic_chess_knight_horse),
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .dragAndDropSource {
                                    detectTapGestures(
                                        onLongPress = {
                                            startTransfer(
                                                DragAndDropTransferData(
                                                    ClipData.newPlainText(
                                                        "object",
                                                        sessionConfig.board.horse.toString()
                                                    )
                                                )
                                            )
                                        }
                                    )
                                }


                        )
                    } else if (sessionConfig.board.target == box) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "",
                            tint = if (box.isDark) Color.White else Color.Black,
                            modifier = Modifier
                                .fillMaxSize()
                                .dragAndDropSource {
                                    detectTapGestures(
                                        onLongPress = {
                                            startTransfer(
                                                DragAndDropTransferData(
                                                    ClipData.newPlainText(
                                                        "object",
                                                        sessionConfig.board.target.toString()
                                                    )
                                                )
                                            )
                                        }
                                    )
                                }


                        )
                    }
                }
            }

            Button(
                modifier = Modifier
                    .constrainAs(calcButton) {
                        bottom.linkTo(parent.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                    },
                enabled = sessionConfig.board.horse != null && sessionConfig.board.target != null,
                onClick = onCalculateClicked
            ) {
                if (calculating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(10.dp),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = stringResource(R.string.calculating))
                } else {
                    Text(text = stringResource(R.string.calculatePaths))
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
            BoardScreenContent(
                sessionConfig = SessionConfig(Board(1, null, null, 8)),
                calculating = true,
                onBoxClicked = {},
                onItemDropped = { i, b -> },
                onConfigComplete = {},
                onCalculateClicked = {}
            )
        }
    }
}