package com.vasberc.presentation.model

data class Session(
    val boardSize: Int,
    val maxMoves: Int,
    val horseBox: Pair<Int, Int>? = null,
    val targetBox: Pair<Int, Int>? = null
)