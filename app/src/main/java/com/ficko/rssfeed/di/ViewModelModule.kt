package com.ficko.rssfeed.di

import com.ficko.rssfeed.vm.NavigationViewModel
import com.ficko.rssfeed.vm.RssFeedViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class ViewModelModule {

    @Provides
    fun provideRssFeedViewModel() = RssFeedViewModel()

    @Provides
    fun provideNavigationViewModel() = NavigationViewModel()
}