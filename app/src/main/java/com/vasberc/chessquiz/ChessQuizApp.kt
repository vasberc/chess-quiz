package com.vasberc.chessquiz

import android.app.Application
import com.vasberc.data_local.di.DataLocalModule
import com.vasberc.presentation.di.PresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module
import timber.log.Timber

class ChessQuizApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initTimber()
        startKoin()
    }

    private fun startKoin() {
        startKoin {
            androidContext(this@ChessQuizApp)
            modules(
                DataLocalModule().module,
                PresentationModule().module
            )
        }
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}