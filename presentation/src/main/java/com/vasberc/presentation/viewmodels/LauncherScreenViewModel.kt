package com.vasberc.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.vasberc.presentation.repo.SessionRepo
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LauncherScreenViewModel(
    private val sessionRepo: SessionRepo
): ViewModel() {
}