package com.vasberc.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.vasberc.presentation.navigation.ChessQuizRoutes.BoardScreen
import com.vasberc.presentation.uimodels.Box
import com.vasberc.presentation.uimodels.Horse
import com.vasberc.presentation.uimodels.SessionConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class BoardScreenViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val isResume = savedStateHandle.toRoute<BoardScreen>().isResume

    private val _sessionConfig = MutableStateFlow<SessionConfig?>(null)
    val sessionConfig = _sessionConfig.asStateFlow()

    val hasHorse: Boolean
        get() = _sessionConfig.value?.board?.horse != null

    val hasTarget: Boolean
        get() = _sessionConfig.value?.board?.target != null


    fun setSessionConfig(config: SessionConfig) {
        _sessionConfig.value = config
    }

    fun onBoxClicked(index: Int) {
        val sessionConfig = _sessionConfig.value

        if(sessionConfig == null) return

        _sessionConfig.update {
            when{
                !hasHorse -> {
                    sessionConfig.copy(
                        board = sessionConfig.board.copy(
                            horse = Horse(sessionConfig.board.boxes[index])
                        )
                    )
                }
                !hasTarget -> {
                    sessionConfig.copy(
                        board = sessionConfig.board.copy(
                            target = Box(sessionConfig.board.boxes[index].x, sessionConfig.board.boxes[index].y)
                        )
                    )
                }
                else -> sessionConfig
            }
        }
    }

    fun onItemDropped(index: Int, isHorse: Boolean) {
        val sessionConfig = _sessionConfig.value

        if(sessionConfig == null) return

        _sessionConfig.value =
            if(isHorse) {
                sessionConfig.copy(
                    board = sessionConfig.board.copy(
                        horse = Horse(sessionConfig.board.boxes[index])
                    )
                )
            } else {
                sessionConfig.copy(
                    board = sessionConfig.board.copy(
                        target = Box(sessionConfig.board.boxes[index].x, sessionConfig.board.boxes[index].y)
                    )
                )
            }
    }


}