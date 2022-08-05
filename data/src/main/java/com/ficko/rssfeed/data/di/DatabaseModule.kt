package com.ficko.rssfeed.data.di

import android.app.Application
import androidx.room.Room
import com.ficko.rssfeed.data.local.database.FavoritesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    private val databaseName = "favorites-db"

    @Provides
    @Singleton
    fun provideFavoritesDatabase(application: Application): FavoritesDatabase =
        Room.databaseBuilder(application, FavoritesDatabase::class.java, databaseName)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideRssFeedDao(database: FavoritesDatabase) = database.rssFeedDao()
}