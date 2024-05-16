package com.vasberc.presentation.repo

import com.vasberc.presentation.model.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepo {
    fun getSessionFlow(): Flow<Session?>
}