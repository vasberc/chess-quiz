package com.vasberc.presentation.componets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.graphics.Color
import com.vasberc.presentation.uimodels.SessionConfig

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