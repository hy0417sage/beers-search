package com.flitto.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiBeersResponse(
    @field:Json(name = "name") val name: String?, /* 이름 */
    @field:Json(name = "tagline") val tagline: String?, /* 슬로건 */
    @field:Json(name = "description") val description: String?, /* 설명 */
    @field:Json(name = "image_url") val image_url: String?, /* 이미지 URL */
)
