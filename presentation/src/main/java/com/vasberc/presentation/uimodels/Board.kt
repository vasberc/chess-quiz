package com.vasberc.presentation.uimodels

import kotlin.math.pow

data class Board(
    val moves: Int,
    val horse: Horse?,
    val target: Box?,
    val boardSize: Int
) {
    val boxes = Array(boardSize.toDouble().pow(2).toInt()) { index: Int ->
        val y = index / boardSize
        val x = if(y == 0) {
            index
        } else {
            index % boardSize
        }
        Box(x, y)
    }

}