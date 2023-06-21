package com.flitto.domain.repository

import androidx.paging.PagingData
import com.flitto.core.SearchItem
import kotlinx.coroutines.flow.Flow

interface SearchBeersRepository {
    suspend fun searchBeers(query: String): Flow<PagingData<SearchItem>>
}