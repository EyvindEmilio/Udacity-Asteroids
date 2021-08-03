package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// TO scalar responses
private val scalarRetrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .build()

// To auto convert to json classes
private val jsonRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    fun getFeed(
        @Query("start_date") startDate: String,
        @Query("api_key") apiKey: String
    ): Call<String>

    @GET("planetary/apod")
    fun getTodayPicture(
        @Query("date") date: String,
        @Query("api_key") apiKey: String
    ): Call<PictureOfDay>
}

object AsteroidApi {
    val scalarService: AsteroidApiService by lazy { scalarRetrofit.create(AsteroidApiService::class.java) }
    val jsonService: AsteroidApiService by lazy { jsonRetrofit.create(AsteroidApiService::class.java) }
}