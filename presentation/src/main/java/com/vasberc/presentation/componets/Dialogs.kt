package com.vasberc.presentation.componets

import android.graphics.Paint
import android.graphics.Paint.Align
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.vasberc.presentation.R
import com.vasberc.presentation.uimodels.Box

@Composable
fun PathsDialog(
    onDismissRequest: () -> Unit,
    shape: Shape = Shapes().medium.copy(all = CornerSize(8.dp)),
    properties: DialogProperties = DialogProperties(),
    paths: List<List<Box>>,
    isVisible: Boolean
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = properties
        ) {
            Column(
                modifier = Modifier
                    .background(Color.Black, shape)
                    .padding(5.dp)
            ) {
                Text(
                    stringResource(R.string.paths_found),
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(
                    state = rememberLazyListState()
                ) {
                    items(paths.size, key = { paths[it].toString() }) { index ->
                        Text(
                            text = paths[index].toString().trim('[', ']'),
                            modifier = Modifier.fillMaxWidth().background(Color.Gray, shape).padding(5.dp),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun PathsDialogPreview() {
    PathsDialog(onDismissRequest = {}, isVisible = true, paths = listOf(listOf(Box(1, 2))))
}