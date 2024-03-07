package com.example.newsapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.newsapp.data.model.Article
import kotlinx.coroutines.flow.Flow
@Dao
interface ArticleDao {

    @Upsert
    suspend fun upsertArticle(article: Article):Long // long mean id was inserted

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("select * from articles")
    fun getAllArticles(): LiveData<List<Article>>


}