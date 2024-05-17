package com.vasberc.presentation.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.vasberc.presentation.R
import com.vasberc.presentation.model.Session

@Composable
fun UserOptionsPopUp(
    onUserSelect: (session: Session) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(10.dp, 0.dp)
            .fillMaxSize()
    ) {
        val column = createRef()
        Column(
            Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                .background(Color.White)
                .padding(25.dp, 15.dp)
                .constrainAs(column) {
                    bottom.linkTo(parent.bottom)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val boardSize = rememberSaveable {
                mutableIntStateOf(-2)
            }

            val sizeHasError by remember {
                derivedStateOf {
                    (boardSize.intValue < 6 || boardSize.intValue > 16)
                            //Prevent show the error is the user did not changed the value
                            && boardSize.intValue != -2
                }
            }

            Column {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = if(boardSize.intValue <= -1) "" else boardSize.intValue.toString(),
                    onValueChange = {
                        boardSize.intValue = it
                            .toIntOrNull()
                            //prevent wrong values by setting -1 if the value is not correct
                            .takeUnless {
                                    value -> value == null || value < 1 || value > 16
                            } ?: -1
                    },
                    label = {
                        Text(
                            text = "Board Size (min: 6, max: 16)"
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number,
                    )
                )
                if(sizeHasError) {
                    Text(
                        text = "Please set a value between 6 and 16",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Default,
                            color = Color.Red
                        )
                    )
                }
            }


            Spacer(modifier = Modifier.height(10.dp))

            val maxMoves = rememberSaveable {
                mutableIntStateOf(3)
            }

            val movesHasError by remember {
                derivedStateOf {
                    (maxMoves.intValue < 1)
                }
            }

            Column {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = if(maxMoves.intValue == -1) "" else maxMoves.intValue.toString(),
                    onValueChange = {
                        maxMoves.intValue = it
                            .toIntOrNull()
                            //prevent wrong values by setting -1 if the value is not correct
                            .takeUnless {
                                    value -> value == null || value < 1
                            } ?: -1
                    },
                    label = {
                        Text(
                            text = "Max moves (min: 1)"
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number,
                    )
                )

                if(movesHasError) {
                    Text(
                        text = "Please set a minimum value of 1",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Default,
                            color = Color.Red
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                modifier = Modifier.align(Alignment.End),
                enabled = boardSize.intValue > -1 &&
                        maxMoves.intValue >= 1 &&
                        !sizeHasError &&
                        !movesHasError,
                onClick = { onUserSelect(Session(boardSize = boardSize.intValue, maxMoves = maxMoves.intValue)) }
            ) {
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = "ok"
                )
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
            UserOptionsPopUp{}
        }
    }
}