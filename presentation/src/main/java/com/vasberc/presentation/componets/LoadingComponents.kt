package com.vasberc.presentation.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LoadingSpinnerWithMask(
    maskColor: Color = Color.White,
    size: Dp = 64.dp,

    ) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(maskColor.copy(alpha = 0.6f))
            .clickable(enabled = false){},
        contentAlignment = Alignment.Center


    ) {
        CircularProgressIndicator(
            Modifier.size(size = size),
            strokeWidth = 8.dp
        )
    }
}

@Preview
@Composable
fun LoadingPrev() {
    LoadingSpinnerWithMask(
        Color.White,
        64.dp
    )
}