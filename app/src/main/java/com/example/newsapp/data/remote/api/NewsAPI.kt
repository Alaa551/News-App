package com.example.newsapp.data.remote.api

import com.example.newsapp.data.model.NewsResponse
import com.example.newsapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("top-headlines")
    suspend fun getBreakingNews(
        @Query("category")
        category: String = "general",

        @Query("lang")
        language: String = "en",

        @Query("country")
        countryCode: String = "us",

        @Query("max")
        maxResults: Int = 50,

        @Query("apikey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("search")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,

        @Query("max")
        maxResults: Int = 10,

        @Query("lang")
        language: String = "en",

        @Query("apikey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

}