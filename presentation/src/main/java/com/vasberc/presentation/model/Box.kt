package com.vasberc.presentation.model

import android.util.Printer
import androidx.compose.ui.graphics.Color

class Box(
    val color: Color,
    val x: Int,
    val y: Int,
    private var hasHorse: Boolean
) {
    fun setHasHorse(b: Boolean) {
        hasHorse = b
    }

    fun getHasHorse() = hasHorse
}