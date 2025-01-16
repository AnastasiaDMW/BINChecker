package com.example.binchecker.di

import android.content.Context
import androidx.room.Room
import com.example.binchecker.data.database.BINCardDatabase
import com.example.binchecker.data.database.dao.BINCardDao
import com.example.binchecker.repository.BINDatabaseRepository
import com.example.binchecker.repository.BINDatabaseRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideBINDao(database: BINCardDatabase): BINCardDao {
        return database.binCardDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): BINCardDatabase {
        return Room.databaseBuilder(
            appContext,
            BINCardDatabase::class.java,
            "cards_db"
        ).build()
    }

    @Provides
    fun provideBINDatabaseRepository(binCardDao: BINCardDao): BINDatabaseRepository {
        return BINDatabaseRepositoryImpl(binCardDao)
    }

}