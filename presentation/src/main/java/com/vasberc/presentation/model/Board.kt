package com.vasberc.presentation.model

import androidx.compose.ui.graphics.Color

class Board(size: Int) {

    private var boxes: List<Box>
//todo
    init {
        val tempBoxes = mutableListOf<Box>()
        for(y in 0 until  size) {
            for (x in 0 until size) {
                tempBoxes.add(
                    Box(
                        color = if((y + x) % 2 == 0) Color.White else Color.Black,
                        x = x,
                        y = y,
                        hasHorse = false
                    )
                )
            }
        }
        boxes = tempBoxes.toList()
    }
}