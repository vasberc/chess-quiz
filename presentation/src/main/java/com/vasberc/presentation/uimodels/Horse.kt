package com.vasberc.presentation.uimodels

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