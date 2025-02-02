package com.vasberc.presentation.componets

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ripple
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.vasberc.presentation.uimodels.Box
import com.vasberc.presentation.uimodels.SessionConfig
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

fun Modifier.clickableWithColor(
    color: Color?,
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val indication = if (color != null) ripple(
        color = color
    ) else {
        null
    }
    this.clickable(
        enabled = enabled,
        interactionSource = interactionSource,
        indication = indication,
        onClick = onClick
    )
}

fun Modifier.zIndexOfBox(
    box: Box,
    sessionConfig: SessionConfig
): Modifier = composed {
    val zIndex by remember(sessionConfig.board.horse) {
        derivedStateOf {
            if (box == sessionConfig.board.horse?.position) {
                1f
            } else {
                0f
            }
        }
    }
    zIndex(zIndex)
}

fun Modifier.moveHorseToBox(
    enabled: Boolean = false,
    targetBox: Box?,
    currentBox: Box,
    onFinished: (Box?) -> Unit = {},
): Modifier = composed {
    val localDensity = LocalDensity.current
    val xPx by remember(targetBox) {
        derivedStateOf {
            with(localDensity) {
                val sing = if ((targetBox?.x ?: 0) > currentBox.x)
                    1
                else if ((targetBox?.x ?: 0) < currentBox.x)
                    -1
                else
                    0
                100.dp.toPx().roundToInt() * sing
            }
        }
    }
    val yPx by remember(targetBox) {
        derivedStateOf {
            with(localDensity) {
                val sing = if ((targetBox?.y ?: 0) > currentBox.y)
                    1
                else if ((targetBox?.y ?: 0) < currentBox.y)
                    -1
                else
                    0
                100.dp.toPx().roundToInt() * sing
            }
        }
    }
    val offset by animateIntOffsetAsState(
        animationSpec = tween(durationMillis = 500),
        targetValue = if (enabled) {
            IntOffset(xPx, yPx)
        } else {
            IntOffset.Zero
        },
        label = "offset",
        finishedListener = {
            onFinished(targetBox)
        }
    )

    offset { offset }
}

fun Modifier.keepOnScreen(
    scrollViewCoordinates: LayoutCoordinates?,
    horizontalScrollState: ScrollState,
    verticalScrollState: ScrollState,
    enabled: Boolean = false
): Modifier = composed {
    val coroutineScope = rememberCoroutineScope()
    val localDensity = LocalDensity.current
    onGloballyPositioned { layoutCoordinates ->
        with(localDensity) {
            val boxLeft =
                layoutCoordinates.positionInRoot().x
            val boxRight =
                boxLeft + layoutCoordinates.size.width
            val boxTop =
                layoutCoordinates.positionInRoot().y
            val boxBottom =
                boxTop + layoutCoordinates.size.height
            val boardLeft =
                scrollViewCoordinates?.positionInRoot()?.x ?: 0.0f
            val boardRight = boardLeft + (scrollViewCoordinates?.size?.width ?: Int.MAX_VALUE)
            val boardTop =
                scrollViewCoordinates?.positionInRoot()?.y ?: 0.0f
            val boardBottom =
                boardTop + (scrollViewCoordinates?.size?.height ?: Int.MAX_VALUE)

            if (enabled) {
                coroutineScope.launch {
                    if (boxLeft < boardLeft) {
                        horizontalScrollState.scrollBy(boxLeft - boardLeft)
                    } else if (boxRight > boardRight) {
                        horizontalScrollState.scrollBy(boxRight - boardRight)
                    } else if (boxTop < boardTop) {
                        verticalScrollState.scrollBy(boxTop - boardTop)
                    } else if (boxBottom > boardBottom) {
                        verticalScrollState.scrollBy(boxBottom - boardBottom)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.dragAndDropTargetElement(
    shouldStartDragAndDrop: (DragAndDropEvent) -> Boolean,
    sessionConfig: SessionConfig,
    index: Int,
    onBackgroundChange: (Color?) -> Unit,
    onItemDropped: (index: Int, isHorse: Boolean) -> Unit
): Modifier = composed {
    val dndTarget = remember(sessionConfig) {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                onBackgroundChange(null)
                val draggedData = event.toAndroidDragEvent()
                    .clipData.getItemAt(0).text
                if (draggedData.toString() == sessionConfig.board.horse.toString()) {
                    onItemDropped(index, true)
                } else if (draggedData.toString() == sessionConfig.board.target.toString()) {
                    onItemDropped(index, false)
                }
                return true
            }

            override fun onEntered(event: DragAndDropEvent) {
                super.onEntered(event)
                onBackgroundChange(Color.Gray)
            }

            override fun onEnded(event: DragAndDropEvent) {
                super.onEntered(event)
                onBackgroundChange(null)
            }

            override fun onExited(event: DragAndDropEvent) {
                super.onEntered(event)
                onBackgroundChange(null)
            }

        }
    }
    dragAndDropTarget(
        shouldStartDragAndDrop = shouldStartDragAndDrop,
        target = dndTarget
    )
}