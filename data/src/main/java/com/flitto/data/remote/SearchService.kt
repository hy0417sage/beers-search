package com.flitto.data.remote

import com.flitto.data.remote.model.ApiBeersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    //https://api.punkapi.com/v2/beers?beer_name=Buzz&page=2
    @GET("v2/beers?")
    suspend fun getService(
        @Query("beer_name") query: String?,
        @Query("page") page : Int,
    ): List<ApiBeersResponse>
}