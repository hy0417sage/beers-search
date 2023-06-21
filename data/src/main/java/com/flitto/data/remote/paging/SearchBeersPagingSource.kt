package com.flitto.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.flitto.data.remote.SearchService
import com.flitto.data.remote.model.ApiMapper
import com.flitto.core.SearchItem
import javax.inject.Inject

private const val SEARCH_STARTING_PAGE_INDEX = 1

class SearchBeersPagingSource @Inject constructor(
    private val service: SearchService,
    private val apiMapper: ApiMapper,
    private val query: String,
) : PagingSource<Int, SearchItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchItem> {
        return try {
            val page = params.key ?: SEARCH_STARTING_PAGE_INDEX

            val items = service.getService(
                query = query,
                page = page,
            ).map { apiMapper.apiBeersToSearchItem(it) }

            /* 페이징 확인 1초 딜레이 */
            //if (page != SEARCH_STARTING_PAGE_INDEX) {
            //    delay(1000)
            //}

            return LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + 1 /* 페이지 넘버값 증가 데이터 여부 확인 */
            )

        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}