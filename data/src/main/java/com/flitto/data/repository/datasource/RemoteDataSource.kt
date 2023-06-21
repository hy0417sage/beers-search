package com.flitto.data.repository.datasource

import androidx.paging.PagingData
import com.flitto.core.SearchItem
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun searchBeers(query: String): Flow<PagingData<SearchItem>>
}