package com.vasberc.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vasberc.presentation.model.ScreenState
import com.vasberc.presentation.model.Session
import com.vasberc.presentation.repo.SessionRepo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class BoardScreenViewModel(
    private val sessionRepo: SessionRepo,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    var screenState by mutableStateOf<ScreenState<BoardScreenState>>(ScreenState.Loading)
        private set

    init {
        val isResume = savedStateHandle.get<Boolean>("isResume") ?: false

        if(isResume) {
            viewModelScope.launch {
                screenState = ScreenState.Success(
                    data = BoardScreenState(
                        session = sessionRepo.getSessionFlow().first()
                    )
                )
            }
        } else {
            screenState = ScreenState.Success(
                data = BoardScreenState()
            )
        }
    }

    fun setSession(session: Session?) {
        screenState = ScreenState.Success(
            data = BoardScreenState(
                session = session
            )
        )
        savedStateHandle["isResume"] = session != null
        viewModelScope.launch {
            sessionRepo.saveSession(session)
        }
    }

    data class BoardScreenState(
        val session: Session? = null
    )
}

