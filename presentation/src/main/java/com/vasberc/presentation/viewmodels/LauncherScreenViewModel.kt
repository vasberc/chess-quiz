package com.vasberc.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.vasberc.presentation.repo.SessionRepo
import kotlinx.coroutines.flow.map
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LauncherScreenViewModel(
    private val sessionRepo: SessionRepo
): ViewModel() {

    val isResumeEnabled = sessionRepo.getSessionFlow().map { it != null }
}