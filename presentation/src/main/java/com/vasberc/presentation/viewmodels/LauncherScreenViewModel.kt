package com.vasberc.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.vasberc.data_local.repo.SessionRepo
import com.vasberc.presentation.uimodels.asSessionConfig
import kotlinx.coroutines.flow.map
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LauncherScreenViewModel(
    sessionRepo: SessionRepo
): ViewModel() {

    val sessionFlow = sessionRepo.getSession().map { it?.asSessionConfig() }
}