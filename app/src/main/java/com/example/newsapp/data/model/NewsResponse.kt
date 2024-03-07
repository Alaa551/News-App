package com.example.newsapp.data.model

data class NewsResponse(
    val articles: MutableList<Article>,
    val totalArticles: Int
)