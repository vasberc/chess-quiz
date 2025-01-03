package com.vasberc.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.vasberc.presentation.navigation.ChessQuizRoutes.BoardScreen
import com.vasberc.presentation.uimodels.Box
import com.vasberc.presentation.uimodels.Horse
import com.vasberc.presentation.uimodels.HorseMovement
import com.vasberc.presentation.uimodels.SessionConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class BoardScreenViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val isResume = savedStateHandle.toRoute<BoardScreen>().isResume

    private val _sessionConfig = MutableStateFlow<SessionConfig?>(null)
    val sessionConfig = _sessionConfig.asStateFlow()

    private val _calculating = MutableStateFlow<Boolean>(false)
    val calculating = _calculating.asStateFlow()

    private val _paths = MutableStateFlow<List<List<Box>>?>(null)
    val paths = _paths.asStateFlow()

    val hasHorse: Boolean
        get() = _sessionConfig.value?.board?.horse != null

    val hasTarget: Boolean
        get() = _sessionConfig.value?.board?.target != null

    fun setSessionConfig(config: SessionConfig) {
        _sessionConfig.value = config
    }

    fun onBoxClicked(index: Int) {
        val sessionConfig = _sessionConfig.value

        if (sessionConfig == null) return

        _sessionConfig.update {
            when {
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
                            target = Box(
                                sessionConfig.board.boxes[index].x,
                                sessionConfig.board.boxes[index].y
                            )
                        )
                    )
                }

                else -> sessionConfig
            }
        }
    }

    fun onItemDropped(index: Int, isHorse: Boolean) {
        val sessionConfig = _sessionConfig.value

        if (sessionConfig == null) return

        _sessionConfig.value =
            if (isHorse) {
                sessionConfig.copy(
                    board = sessionConfig.board.copy(
                        horse = Horse(sessionConfig.board.boxes[index])
                    )
                )
            } else {
                sessionConfig.copy(
                    board = sessionConfig.board.copy(
                        target = Box(
                            sessionConfig.board.boxes[index].x,
                            sessionConfig.board.boxes[index].y
                        )
                    )
                )
            }
    }

    fun onCalculateClicked() {
        val horse = _sessionConfig.value?.board?.horse
        horse?.let {
            _calculating.update { true }
            _paths.update { null }
            viewModelScope.launch(Dispatchers.Default) {
                it.getMoves(_sessionConfig.value!!.board).forEach { box ->
                    checkAndContinue(listOf(box))
                }
                Timber.d("finished paths=${paths.value} \n pathsSize = ${paths.value?.size}")
                _calculating.update { false }
                if (_paths.value.isNullOrEmpty()) {
                    _paths.update { listOf() }
                }
            }
        }
    }

    private suspend fun checkAndContinue(
        path: List<Box>
    ) {
        coroutineScope {
            //Avoid circular paths
            if(path.filter { it == path.last() }.size > 1 ) {
                return@coroutineScope
            }

            if (path.size == sessionConfig.value?.board?.moves && path.last() == sessionConfig.value?.board?.target) {
                //add the path to the succeed paths
                _paths.update { currentValue ->
                    currentValue?.let {
                        it + listOf(path)
                    } ?: listOf(path)
                }
                Timber.d(path.toString())
            } else if (path.size < sessionConfig.value!!.board.moves) {
                //continue checking
                launch(Dispatchers.Default) {
                    Horse(path.last()).getMoves(sessionConfig.value!!.board).forEach { box ->
                        checkAndContinue(path + box)
                    }
                }
            }
        }

    }
}

