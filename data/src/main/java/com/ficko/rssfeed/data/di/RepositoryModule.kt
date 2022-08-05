package com.ficko.rssfeed.data.di

import com.ficko.rssfeed.data.RssFeedRepository
import com.ficko.rssfeed.data.local.database.dao.RssFeedDao
import com.ficko.rssfeed.data.remote.apis.RssFeedApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRssFeedRepository(dao: RssFeedDao, api: RssFeedApi) = RssFeedRepository(dao, api)
}