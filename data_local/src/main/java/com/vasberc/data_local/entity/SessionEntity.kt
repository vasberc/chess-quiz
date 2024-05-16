package com.vasberc.data_local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vasberc.presentation.model.ModelInterface
import com.vasberc.presentation.model.Session

@Entity(tableName = "session")
data class SessionEntity(
    @PrimaryKey
    val id: Int = 1,
    val boardSize: Int
): ModelInterface<Session> {
    override fun asModel(): Session {
        return Session(
            boardSize = boardSize
        )
    }

}

fun Session.asEntity(): SessionEntity {
    return SessionEntity(
        boardSize = boardSize
    )
}