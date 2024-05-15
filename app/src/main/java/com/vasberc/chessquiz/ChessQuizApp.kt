package com.vasberc.chessquiz

import android.app.Application
import timber.log.Timber

class ChessQuizApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}