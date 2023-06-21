package com.flitto.data.repository.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.flitto.core.Constants.PAGE_SIZE
import com.flitto.core.SearchItem
import com.flitto.data.remote.SearchService
import com.flitto.data.remote.model.ApiMapper
import com.flitto.data.remote.paging.SearchBeersPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val service: SearchService,
    private val apiMapper: ApiMapper,
): RemoteDataSource {

    override suspend fun searchBeers(query: String): Flow<PagingData<SearchItem>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                initialLoadSize = PAGE_SIZE,
                pageSize = PAGE_SIZE
            ),
            pagingSourceFactory = { SearchBeersPagingSource(service, apiMapper, query = query) }
        ).flow
    }
}