package com.vasberc.data_local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vasberc.data_local.entity.SessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Query("SELECT * FROM session WHERE id = 1")
    fun getSessionFlow(): Flow<SessionEntity?>

    @Upsert
    suspend fun saveSession(sessionEntity: SessionEntity)

    @Query("DELETE FROM session")
    suspend fun clearSession()
}