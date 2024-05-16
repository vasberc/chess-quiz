package com.vasberc.presentation.model

interface ModelInterface<T: Any> {
    fun asModel(): T
}