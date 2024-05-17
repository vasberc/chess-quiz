package com.vasberc.presentation.model

sealed class ScreenState<out T: Any> {
    data object Loading: ScreenState<Nothing>()
    data class Success<T: Any>(val data: T): ScreenState<T>()
    data class Error(val message: String): ScreenState<Nothing>()
}