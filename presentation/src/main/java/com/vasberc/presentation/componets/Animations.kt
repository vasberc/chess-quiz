package com.vasberc.presentation.componets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun AnimateVisibilityY(isVisible: Boolean, content: @Composable () -> Unit) {

    //False initially to not render the pop up so the enter animation will take effect
    var showPopUp by remember {
        mutableStateOf(false)
    }

    AnimatedVisibility(
        enter = slideInVertically(
            initialOffsetY = {
                //To start out of the screen the offset is set to the height of the component
                it
            },
        ),
        exit = slideOutVertically(
            targetOffsetY = {
                //Same with initial starting logic to go out of the screen
                it
            },
        ),
        visible = isVisible && showPopUp
    ) {
        content()
    }

    //After the component is rendered we change the visibility
    LaunchedEffect(key1 = null) {
        showPopUp = true
    }
}