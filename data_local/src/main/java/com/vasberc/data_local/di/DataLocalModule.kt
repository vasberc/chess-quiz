package com.vasberc.data_local.di

import android.content.Context
import androidx.room.Room
import com.vasberc.data_local.dao.SessionDao
import com.vasberc.data_local.db.ChessQuizDb
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.vasberc.data_local")
class DataLocalModule

@Single
fun provideDb(context: Context): ChessQuizDb {
    return Room.databaseBuilder(
        context = context,
        klass = ChessQuizDb::class.java,
        name = "chess_quiz_db"
    ).build()
}

@Single
fun provideSessionDao(dataBase: ChessQuizDb): SessionDao {
    return dataBase.sessionDao()
}