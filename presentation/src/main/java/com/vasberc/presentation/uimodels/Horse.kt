package com.vasberc.presentation.uimodels

import kotlin.math.abs

data class Horse(
    val position: Box
) {
    fun getMoves(board: Board): List<Box> {
        return listOf(
                position.getBoxAbove().getBoxAbove().getBoxLeft().takeIf { board.isValidBox(it) },
                position.getBoxAbove().getBoxAbove().getBoxRight().takeIf { board.isValidBox(it) },
                position.getBoxRight().getBoxRight().getBoxAbove().takeIf { board.isValidBox(it) },
                position.getBoxRight().getBoxRight().getBoxBelow().takeIf { board.isValidBox(it) },
                position.getBoxBelow().getBoxBelow().getBoxLeft().takeIf { board.isValidBox(it) },
                position.getBoxBelow().getBoxBelow().getBoxRight().takeIf { board.isValidBox(it) },
                position.getBoxLeft().getBoxLeft().getBoxAbove().takeIf { board.isValidBox(it) },
                position.getBoxLeft().getBoxLeft().getBoxBelow().takeIf { board.isValidBox(it) }

        ).filterNotNull()
    }

    fun move(to: Box): Horse {
        return Horse(position = to)
    }

    companion object {
        fun findBoxesPath(from: Box, to: Box): Array<Box> {
            val path = Array<Box>(3) { Box(-1, -1) }
            val shouldStartX = abs(from.x - to.x) == 2
            val shouldGoRight = from.x - to.x < 0
            val shouldGoDown = from.y - to.y < 0
            if (shouldStartX) {
                if (shouldGoRight) {
                    path[0] = HorseMovement.RIGHT.move(from)
                    path[1] = HorseMovement.RIGHT.move(path[0])
                } else {
                    path[0] = HorseMovement.LEFT.move(from)
                    path[1] = HorseMovement.LEFT.move(path[0])

                }
                path[2] = if (shouldGoDown) HorseMovement.DOWN.move(path[1]) else HorseMovement.UP.move(path[1])
            } else {
                if (shouldGoDown) {
                    path[0] = HorseMovement.DOWN.move(from)
                    path[1] = HorseMovement.DOWN.move(path[0])
                } else {
                    path[0] = HorseMovement.UP.move(from)
                    path[1] = HorseMovement.UP.move(path[0])
                }
                path[2] = if (shouldGoRight) HorseMovement.RIGHT.move(path[1]) else HorseMovement.LEFT.move(path[1])
            }
            return path
        }
    }
}

sealed class HorseMovement() {

    abstract fun move(from: Box): Box

    data object UP: HorseMovement() {
        override fun move(from: Box): Box {
            return from.getBoxAbove()
        }
    }

    data object DOWN: HorseMovement() {
        override fun move(from: Box): Box {
            return from.getBoxBelow()
        }
    }

    data object LEFT: HorseMovement() {
        override fun move(from: Box): Box {
            return from.getBoxLeft()
        }
    }

    data object RIGHT: HorseMovement() {
        override fun move(from: Box): Box {
            return from.getBoxRight()
        }
    }

}