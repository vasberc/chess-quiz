package com.vasberc.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.vasberc.data_local.repo.SessionRepo
import com.vasberc.presentation.navigation.ChessQuizRoutes.BoardScreen
import com.vasberc.presentation.uimodels.Box
import com.vasberc.presentation.uimodels.Horse
import com.vasberc.presentation.uimodels.SessionConfig
import com.vasberc.presentation.uimodels.asEntity
import com.vasberc.presentation.uimodels.asSessionConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber
import java.util.LinkedList

@KoinViewModel
class BoardScreenViewModel(
    private val sessionRepo: SessionRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val isResume = savedStateHandle.toRoute<BoardScreen>().isResume

    private val _sessionConfig = MutableStateFlow<SessionConfig?>(null)
    val sessionConfig = _sessionConfig.asStateFlow()

    private val _calculating = MutableStateFlow<Boolean>(false)
    val calculating = _calculating.asStateFlow()

    private var selectedPath: List<Box>? = null

    private val _movingToBox = MutableStateFlow<Box?>(null)
    val movingToBox = _movingToBox.asStateFlow()

    private val _paths = MutableStateFlow<List<List<Box>>?>(null)
    val paths = _paths.asStateFlow()

    private val list: LinkedList<Box> = LinkedList()

    val hasHorse: Boolean
        get() = _sessionConfig.value?.board?.horse != null

    val hasTarget: Boolean
        get() = _sessionConfig.value?.board?.target != null

    init {
        if (isResume) {
            viewModelScope.launch {
                _sessionConfig.update { sessionRepo.getSession().first()?.asSessionConfig() }
            }
        }
    }

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
        selectedPath = null

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
        if (calculating.value) {
            onCalculateClicked()
        } else {
            _calculating.update { false }
            _paths.update { null }
        }
    }

    fun onCalculateClicked() {
        val horse = _sessionConfig.value?.board?.horse ?: return
        val target = _sessionConfig.value?.board?.target ?: return
        val gameMoves = _sessionConfig.value?.board?.moves ?: return

        _calculating.update { true }
        _paths.update { null }

        val isHorseOnDarkBox = horse.position.isDark
        val isTargetOnDarkBox = target.isDark
        val areSameColor = isHorseOnDarkBox == isTargetOnDarkBox
        val areMovesEven = gameMoves % 2 == 0
        val canReachTarget = areMovesEven && areSameColor || !areMovesEven && !areSameColor
        if (!canReachTarget) {
            _calculating.update { false }
            _paths.update { listOf() }
            return
        }
        viewModelScope.launch(Dispatchers.Default) {
            horse.getMoves(_sessionConfig.value!!.board).forEach { box ->
                checkAndContinue(listOf(horse.position, box))
            }
            Timber.d("finished paths=${paths.value} \n pathsSize = ${paths.value?.size}")
            _calculating.update { false }
            if (_paths.value.isNullOrEmpty()) {
                _paths.update { listOf() }
            }
        }
    }

    private fun checkAndContinue(
        path: List<Box>
    ) {
        //Avoid circular paths
        if (path.filter { it == path.last() }.size > 1) {
            return
        }

        if (path.size - 1 == sessionConfig.value?.board?.moves && path.last() == sessionConfig.value?.board?.target) {
            //add the path to the succeed paths
            _paths.update { currentValue ->
                currentValue?.let {
                    it + listOf(path)
                } ?: listOf(path)
            }
            Timber.d(path.toString())
        } else if (path.size - 1 < sessionConfig.value!!.board.moves) {
            //continue checking
            Horse(path.last()).getMoves(sessionConfig.value!!.board).forEach { box ->
                checkAndContinue(path + box)
            }
        }
    }

    fun onPathClicked(path: List<Box>) {
        selectedPath = path
        list.clear()

        viewModelScope.launch(Dispatchers.Default) {
            for (i in 1 until path.size) {
                calcHorseMoves(path[i - 1], path[i])
            }
            if (sessionConfig.value?.board?.horse?.position != path[0]) {
                _sessionConfig.update {
                    it?.copy(
                        board = it.board.copy(
                            horse = Horse(path[0])
                        )
                    )
                }
                delay(500)
            }
            _movingToBox.update { list.pop() }
        }
    }

    private fun calcHorseMoves(from: Box, to: Box) {
        Horse.findBoxesPath(from, to).forEach { box ->
            list.add(box)
        }
    }

    fun onHorseMoved(box: Box?) {
        if (box == null) return
        _movingToBox.update { null }
        _sessionConfig.update {
            _sessionConfig.value?.copy(
                board = _sessionConfig.value?.board!!.copy(
                    horse = Horse(box)
                )

            )
        }
        viewModelScope.launch {
            delay(500)
            if (list.isNotEmpty()) {
                _movingToBox.update { list.pop() }
            } else {
                selectedPath = null
            }
        }
    }

    override fun onCleared() {
        CoroutineScope(Dispatchers.IO).launch {
            _sessionConfig.value?.let { sessionRepo.insertSession(it.asEntity()) }
        }
        super.onCleared()
    }
}

