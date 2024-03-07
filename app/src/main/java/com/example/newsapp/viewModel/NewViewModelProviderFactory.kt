package com.example.newsapp.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.repository.NewsRepository

class NewViewModelProviderFactory(
    val app: Application,
    private val newsRepository: NewsRepository,
    val lang:String,
    val countryCode:String
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app,newsRepository, lang = lang, countryCode = countryCode) as T
    }


}