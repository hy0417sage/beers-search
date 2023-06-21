package com.flitto.data.remote.model

import android.content.Context
import com.flitto.core.SearchItem
import com.flitto.core.isBookmarked
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ApiMapper @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun apiBeersToSearchItem(apiEntity: ApiBeersResponse?): SearchItem {
        return SearchItem(
            name = apiEntity?.name.orEmpty(),
            tagline = apiEntity?.tagline.orEmpty(),
            description = apiEntity?.description.orEmpty(),
            image_url = apiEntity?.image_url.orEmpty(),
            bookmarked = context.isBookmarked(apiEntity?.name.orEmpty())
        )
    }
}