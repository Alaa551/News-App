package com.example.newsapp.ui.adapter

import com.example.newsapp.data.model.Article

interface OnArticleClickListener {
    fun onItemClick(article: Article)
}
