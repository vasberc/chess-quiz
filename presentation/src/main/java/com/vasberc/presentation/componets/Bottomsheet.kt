package com.vasberc.presentation.componets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vasberc.presentation.R
import com.vasberc.presentation.uimodels.Board
import com.vasberc.presentation.uimodels.SessionConfig
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onButtonPress: (sessionConfig: SessionConfig) -> Unit
) {
    val scope = rememberCoroutineScope()
    var moves by remember { mutableIntStateOf(-1) }
    var boardSize by remember { mutableIntStateOf(-1) }
    val validValues = remember {
        derivedStateOf {
            moves > 0 && boardSize >=6 && boardSize <= 16
        }
    }
    val sheetState = rememberModalBottomSheetState { validValues.value }

    ModalBottomSheet(
        onDismissRequest = {
        },
        sheetState = sheetState,
        properties = ModalBottomSheetProperties(false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.select_preferences),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(15.dp))
            OutlinedTextField(
                isError = moves <= 0,
                value = moves.takeUnless { it == -1 }?.toString() ?: "",
                onValueChange = {
                    if (it.isEmpty() && moves != -1) {
                        moves = -1
                    } else if ((it.toIntOrNull() ?: -1) != moves) {
                        moves = it.toIntOrNull() ?: moves
                    }
                },
                label = { Text(stringResource(R.string.insert_max_moves)) })
            Spacer(Modifier.height(5.dp))
            OutlinedTextField(
                isError = boardSize < 6 || boardSize > 16,
                value = boardSize.takeUnless { it == -1 }?.toString() ?: "",
                onValueChange = {
                    if (it.isEmpty() && boardSize != -1) {
                        boardSize = -1
                    } else if ((it.toIntOrNull() ?: -1) != boardSize) {
                        boardSize = it.toIntOrNull() ?: boardSize
                    }
                },
                label = { Text(stringResource(R.string.insert_board_size)) })
            Spacer(Modifier.height(5.dp))
            if (!validValues.value) {
                Text(
                    stringResource(R.string.invalid_config),
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    textDecoration = TextDecoration.Underline,
                    color = Color.Red
                )
                Spacer(Modifier.height(5.dp))
            }
            Button(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    if (validValues.value) {
                        scope.launch {
                            sheetState.hide()
                            onButtonPress(
                                SessionConfig(
                                    board = Board(
                                        moves = moves,
                                        horse = null,
                                        target = null,
                                        boardSize = boardSize
                                    )
                                )
                            )
                        }
                    }
                }
            ) {
                Spacer(modifier = Modifier.width(5.dp))
                Text(stringResource(R.string.ok))
                Spacer(modifier = Modifier.width(5.dp))
            }
            Spacer(Modifier.height(5.dp))
        }
    }
}