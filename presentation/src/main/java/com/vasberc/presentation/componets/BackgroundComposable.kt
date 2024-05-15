package com.vasberc.presentation.componets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.vasberc.presentation.R

@Composable
fun BackgroundComposable(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .safeContentPadding()
            .paint(
                painter = painterResource(id = R.drawable.ic_chess),
                contentScale = ContentScale.Crop
            )
    ) {
        content()
    }
}