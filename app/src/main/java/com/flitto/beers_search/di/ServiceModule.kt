package com.flitto.beers_search.di

import com.flitto.data.remote.SearchClient
import com.flitto.data.remote.SearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object ServiceModule {

    @Provides
    fun provideService(): SearchService {
        return SearchClient.create()
    }
}