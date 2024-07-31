package com.ctyeung.dictatekotlin.di

import android.content.Context
import androidx.room.Room
import com.ctyeung.dictatekotlin.room.VerseDao
import com.ctyeung.dictatekotlin.room.VerseDatabase
import com.ctyeung.dictatekotlin.room.VerseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    fun provideMovieRepository(dao: VerseDao): VerseRepository {
        return VerseRepository(verseDao = dao)
    }

    @Provides
    fun provideVerseDao(db: VerseDatabase): VerseDao {
        return db.verseDao()
    }

    @Provides
    @Singleton
    fun providesMovieDatabase(@ApplicationContext appContext: Context): VerseDatabase {
        return Room.databaseBuilder(appContext, VerseDatabase::class.java, "movie_database").build()
    }
}