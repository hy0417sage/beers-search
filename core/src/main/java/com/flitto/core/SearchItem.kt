package com.flitto.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchItem(
    val name: String,
    val tagline: String,
    val description: String,
    val image_url: String,
    var bookmarked: Boolean,
) : Parcelable