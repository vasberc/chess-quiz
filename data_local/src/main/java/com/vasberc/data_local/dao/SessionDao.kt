package com.vasberc.data_local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vasberc.data_local.entity.SessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionEntity)

    @Query("DELETE FROM session")
    suspend fun deleteAllSessions()

    @Query("SELECT * FROM session LIMIT 1")
    fun getSession(): Flow<SessionEntity?>
}