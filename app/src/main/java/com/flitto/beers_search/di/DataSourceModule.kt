package com.flitto.beers_search.di

import com.flitto.data.remote.model.ApiMapper
import com.flitto.data.remote.SearchService
import com.flitto.data.repository.datasource.RemoteDataSource
import com.flitto.data.repository.datasource.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataSourceModule {

    @Singleton
    @Provides
    fun provideGithubDataSource(
        searchService: SearchService,
        apiMapper: ApiMapper,
    ): RemoteDataSource {
        return RemoteDataSourceImpl(searchService, apiMapper)
    }
}