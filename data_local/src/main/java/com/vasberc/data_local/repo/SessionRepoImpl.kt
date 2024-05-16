package com.vasberc.data_local.repo

import com.vasberc.data_local.dao.SessionDao
import com.vasberc.presentation.repo.SessionRepo
import org.koin.core.annotation.Single

@Single
class SessionRepoImpl(
    private val sessionDao: SessionDao
): SessionRepo {

}