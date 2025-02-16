package com.vasberc.data_local.repo

import com.vasberc.data_local.entity.SessionEntity
import kotlinx.coroutines.flow.Flow

interface SessionRepo {
    suspend fun insertSession(session: SessionEntity)
    fun getSession(): Flow<SessionEntity?>
}