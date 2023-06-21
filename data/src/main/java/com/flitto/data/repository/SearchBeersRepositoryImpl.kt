package com.flitto.data.repository

import androidx.paging.PagingData
import com.flitto.core.SearchItem
import com.flitto.data.repository.datasource.RemoteDataSource
import com.flitto.domain.repository.SearchBeersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchBeersRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : SearchBeersRepository {

    override suspend fun searchBeers(query: String): Flow<PagingData<SearchItem>> {
        return remoteDataSource.searchBeers(query)
    }
}