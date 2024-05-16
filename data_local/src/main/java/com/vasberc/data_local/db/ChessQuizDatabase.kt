package com.vasberc.data_local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vasberc.data_local.dao.SessionDao

@Database(
    entities = [],
    version = 1,
    exportSchema = false
)
abstract class ChessQuizDatabase: RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}