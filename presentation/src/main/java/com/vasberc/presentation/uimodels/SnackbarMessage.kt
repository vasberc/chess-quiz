package com.vasberc.presentation.uimodels

import androidx.compose.material3.SnackbarDuration

data class SnackbarMessage(val message: String, val duration: SnackbarDuration = SnackbarDuration.Long)