package com.flitto.core

import android.content.Context
import com.flitto.core.Constants.BOOKMARKED_LIST
import com.flitto.core.Constants.SHARED_PREFERENCES
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

fun Context.saveBookmarkedList(arrayList: ArrayList<String>, key: String) {
    val editor = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).edit()
    val adapter = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        .adapter<List<String>>(Types.newParameterizedType(List::class.java, String::class.java))
    editor.putString(key, adapter.toJson(arrayList))
    editor.apply()
}

fun Context.getBookmarkedList(key: String): ArrayList<String> {
    val json = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(key, null)
        ?: return arrayListOf()
    val adapter = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        .adapter<List<String>>(Types.newParameterizedType(List::class.java, String::class.java))
    return adapter.fromJson(json)?.let { ArrayList(it) } ?: arrayListOf()
}

fun Context.addBookmarkedToList(item: String) {
    val list = getBookmarkedList(BOOKMARKED_LIST)
    list.add(item)
    saveBookmarkedList(list, BOOKMARKED_LIST)
}

fun Context.removeBookmarkedFromList(item: String): Boolean {
    val list = getBookmarkedList(BOOKMARKED_LIST)
    list.forEachIndexed { index, value ->
        if (item == value) {
            list.removeAt(index)
            saveBookmarkedList(list, BOOKMARKED_LIST)
            return true
        }
    }
    return false
}

fun Context.isBookmarked(name: String): Boolean {
    val list = getBookmarkedList(BOOKMARKED_LIST)
    list.forEach { value ->
        if (name == value)
            return true
    }
    return false
}