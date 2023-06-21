package com.flitto.beers_search.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.flitto.domain.usecase.SearchBeersUseCase
import com.flitto.core.SearchItem
import com.flitto.core.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchBeersUseCase: SearchBeersUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _pagingData = MutableSharedFlow<PagingData<SearchItem>>()
    val pagingData = _pagingData.asSharedFlow()

    /* 페이지를 불러오는 함수 */
    fun searchBeers(beerName: String) = viewModelScope.launch {
        _uiState.update { it.copy(isGuideMessageVisible = false) }
        _uiState.update { it.copy(isLoading = true) }
        _uiState.update { it.copy(currentQuery = beerName) }
        searchBeersUseCase.searchBeers(query = beerName)
            .cachedIn(viewModelScope).collect {
                _pagingData.emit(it)
            }
    }

    fun setProgressBar(isVisible: Boolean){
        if(isVisible){
            _uiState.update { it.copy(isLoading = true) }
        }else{
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}