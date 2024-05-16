package com.vasberc.data_local.repo

import com.vasberc.data_local.dao.SessionDao
import com.vasberc.presentation.model.Session
import com.vasberc.presentation.repo.SessionRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class SessionRepoImpl(
    private val sessionDao: SessionDao
): SessionRepo {
    override fun getSessionFlow(): Flow<Session?> {
        return sessionDao.getSessionFlow().map { it?.asModel() }
    }

}