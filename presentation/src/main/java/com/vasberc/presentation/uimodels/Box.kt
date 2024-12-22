package com.vasberc.presentation.uimodels

data class Box(
    val x: Int,
    val y: Int
) {
    val isDark: Boolean
        get() = x % 2 == 0 && y % 2 == 0 ||
                x % 2 != 0 && y % 2 != 0

    fun isTarget(target: Box): Boolean = this == target

    fun hasHorse(horse: Horse): Boolean = this == horse.position
}