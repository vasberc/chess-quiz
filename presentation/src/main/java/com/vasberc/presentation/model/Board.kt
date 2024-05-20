package com.vasberc.presentation.model

import androidx.compose.ui.graphics.Color

class Board(session: Session) {

    private val size: Int = session.boardSize
    private var boxes: List<Box>

    init {
        val tempBoxes = mutableListOf<Box>()
        for(x in 0 until  size) {
            for (y in 0 until size) {
                tempBoxes.add(
                    Box(
                        color = if((y + x) % 2 == 0) Color.White else Color.Black,
                        x = x,
                        y = y
                    ).also {
                        if(session.horseBox?.first == x && session.horseBox.second == y) {
                            it.setHasHorse(true)
                        } else if(session.targetBox?.first == x && session.targetBox.second == y) {
                            it.setIsTarget(true)
                        }
                    }
                )
            }
        }
        boxes = tempBoxes.toList()
    }
    fun getSize() = size
    fun getBoxes() = boxes
}