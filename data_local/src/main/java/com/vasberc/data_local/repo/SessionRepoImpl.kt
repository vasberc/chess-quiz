package com.vasberc.data_local.repo

import com.vasberc.data_local.dao.SessionDao
import com.vasberc.data_local.entity.SessionEntity
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class SessionRepoImpl(private val sessionDao: SessionDao): SessionRepo {

    override suspend fun insertSession(session: SessionEntity) {
        sessionDao.deleteAllSessions()
        sessionDao.insertSession(session)
    }

    override fun getSession(): Flow<SessionEntity?> = sessionDao.getSession()
}