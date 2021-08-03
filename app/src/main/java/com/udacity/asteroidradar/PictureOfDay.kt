package com.udacity.asteroidradar

import com.squareup.moshi.Json

data class PictureOfDay(
    val date: String,
    @Json(name = "media_type") val mediaType: String,
    val title: String,
    val url: String
) {
    override fun toString(): String {
        return "PictureOfDay(date='$date', mediaType='$mediaType', title='$title', url='$url')"
    }
}