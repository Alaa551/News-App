package com.example.newsapp.repository

import androidx.lifecycle.LiveData
import com.example.newsapp.data.local.ArticleDatabase
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.model.NewsResponse
import com.example.newsapp.data.remote.api.RetrofitInstance
import retrofit2.Response

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode:String,pageNumber:Int,lang:String): Response<NewsResponse> =
        RetrofitInstance.api.getBreakingNews(countryCode= countryCode, maxResults = pageNumber, language = lang)


    suspend fun getCategoryNews(countryCode:String,pageNumber:Int,lang:String,category: String): Response<NewsResponse> =
        RetrofitInstance.api.getBreakingNews(countryCode= countryCode, maxResults = pageNumber, language = lang, category = category)


    suspend fun searchNews(searchQuery:String,pageNumber:Int,lang: String): Response<NewsResponse> =
         RetrofitInstance.api.searchForNews(searchQuery,pageNumber,lang)


    suspend fun upsert(article: Article):Long= db.getArticleDao().upsertArticle(article)

    suspend fun deleteArticle(article: Article)= db.getArticleDao().deleteArticle(article)

     fun getSavedNews(): LiveData<List<Article>> = db.getArticleDao().getAllArticles()

}