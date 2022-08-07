package com.ficko.rssfeed.di

import com.ficko.rssfeed.data.RssFeedRepository
import com.ficko.rssfeed.vm.AppBarViewModel
import com.ficko.rssfeed.vm.RssFeedViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class ViewModelModule {

    @Provides
    fun provideRssFeedViewModel(repository: RssFeedRepository) = RssFeedViewModel(repository)

    @Provides
    fun provideAppBarViewModel() = AppBarViewModel()
}