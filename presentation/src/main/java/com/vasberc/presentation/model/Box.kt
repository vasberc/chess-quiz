package com.vasberc.presentation.model

import androidx.compose.ui.graphics.Color

class Box(
    val color: Color,
    val x: Int,
    val y: Int
) {
    private var hasHorse: Boolean = false
    private var isTarget: Boolean = false

    fun setHasHorse(b: Boolean) {
        hasHorse = b
    }

    fun getHasHorse() = hasHorse

    fun setIsTarget(b: Boolean) {
        isTarget = b
    }

    fun getIsTarget() = isTarget

    override fun toString(): String {
        return "($x, $y)"
    }
}