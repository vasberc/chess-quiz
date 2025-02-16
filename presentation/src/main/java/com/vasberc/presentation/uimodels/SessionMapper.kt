package com.vasberc.presentation.uimodels

import com.vasberc.data_local.entity.SessionEntity

fun SessionEntity.asSessionConfig(): SessionConfig {
    return SessionConfig(
        board = Board(
            moves = moves,
            horse = Horse(
                position = Box(
                    x = horseX ?: 0,
                    y = horseY ?: 0
                )
            ),
            target = Box(
                x = targetX ?: 0,
                y = targetY ?: 0
            ),
            boardSize = boardSize
        )
    )
}

fun SessionConfig.asEntity(): SessionEntity {
    return SessionEntity(
        moves = board.moves,
        horseX = board.horse?.position?.x,
        horseY = board.horse?.position?.y,
        targetX = board.target?.x,
        targetY = board.target?.y,
        boardSize = board.boardSize
    )
}