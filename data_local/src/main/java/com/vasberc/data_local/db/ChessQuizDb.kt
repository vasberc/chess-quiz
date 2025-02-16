package com.vasberc.data_local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vasberc.data_local.dao.SessionDao
import com.vasberc.data_local.entity.SessionEntity

@Database(
    entities = [SessionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ChessQuizDb: RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}