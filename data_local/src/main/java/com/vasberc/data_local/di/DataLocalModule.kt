package com.vasberc.data_local.di

import android.content.Context
import androidx.room.Room
import com.vasberc.data_local.dao.SessionDao
import com.vasberc.data_local.db.ChessQuizDatabase
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.vasberc.data_local")
class DataLocalModule

@Single
fun provideDb(context: Context): ChessQuizDatabase {
    return Room.databaseBuilder(
        context = context,
        klass = ChessQuizDatabase::class.java,
        name = "chess_quiz_db"
    ).build()
}

@Single
fun provideSessionDao(db: ChessQuizDatabase): SessionDao {
    return db.sessionDao()
}