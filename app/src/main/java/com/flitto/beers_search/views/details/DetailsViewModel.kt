package com.flitto.beers_search.views.details

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DetailsViewModel : ViewModel() {

    private val _bookmarkedState = MutableStateFlow(false)
    val bookmarkedState = _bookmarkedState.asStateFlow()

    fun isBookmarked(isBookmarked: Boolean) {
        _bookmarkedState.update { isBookmarked }
    }
}