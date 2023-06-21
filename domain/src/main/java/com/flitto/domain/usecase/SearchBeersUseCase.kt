package com.flitto.domain.usecase

import androidx.paging.PagingData
import com.flitto.domain.repository.SearchBeersRepository
import com.flitto.core.SearchItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *  SearchBeersRepository 의 추상화를 통해 SearchBeersRepositoryImpl 내 함수들 (searchBeers + 추후 추가 함수)
 *  의 내부적 구현이 바뀌더라도 해당 함수들에 의존성을 가지는 Usecase 들 이 영향을 받지 않을 수 있음
 * */

class SearchBeersUseCase @Inject constructor(
    private val repository: SearchBeersRepository
) {
    suspend fun searchBeers(query: String): Flow<PagingData<SearchItem>> {
        return repository.searchBeers(query)
    }
}