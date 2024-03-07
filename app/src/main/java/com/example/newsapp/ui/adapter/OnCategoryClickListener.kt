package com.example.newsapp.ui.adapter

import com.example.newsapp.data.model.Article
import com.example.newsapp.data.model.Category

interface OnCategoryClickListener {
    fun onItemClick(category: Category)

}