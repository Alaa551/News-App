package com.example.newsapp.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.newsapp.data.model.Article

class MyDiffUtil: DiffUtil.ItemCallback<Article>() {


        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url== newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }
