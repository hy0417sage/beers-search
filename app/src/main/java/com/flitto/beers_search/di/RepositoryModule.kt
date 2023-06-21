package com.flitto.beers_search.di

import com.flitto.data.repository.SearchBeersRepositoryImpl
import com.flitto.data.repository.datasource.RemoteDataSource
import com.flitto.domain.repository.SearchBeersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(
        remoteDataSource: RemoteDataSource,
    ): SearchBeersRepository {
        return SearchBeersRepositoryImpl(remoteDataSource)
    }
}