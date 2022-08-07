package com.ficko.rssfeed.data.di

import com.ficko.rssfeed.data.remote.apis.RssFeedApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideRssFeedApi(retrofit: Retrofit) = retrofit.create(RssFeedApi::class.java)
}