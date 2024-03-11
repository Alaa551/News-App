package com.example.newsapp.viewModel

import ConnectivityRepository
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.NewsApplication
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.model.Category
import com.example.newsapp.data.model.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.data.local.sharedPref.SharedPreferencesManager
import com.example.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    app: Application,
    private val newsRepository: NewsRepository,
    val lang: String,
    val countryCode: String,
) : AndroidViewModel(app) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    // we put this in mvvm to stay after rotation
    private var breakingNewsPages = 50


    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    // we put this in mvvm to stay after rotation
    private var searchNewsPages = 50


    val categoryNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()


    val isOnline: LiveData<Boolean> =
        ConnectivityRepository(getApplication<NewsApplication>()).isOnline


    init {
        //getBreakingNews(countryCode, lang)
    }


    fun getBreakingNews(countryCode: String, lang: String) =
        viewModelScope.launch {
            safeBreakingNewsCall(countryCode, lang)

        }

    fun getCategoryNews(countryCode: String, lang: String, category: String) =
        viewModelScope.launch {
            safeCategoryNewsCall(countryCode, lang, category)

        }

    private fun handlingBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { newsResponse ->

                return Resource.Success(newsResponse)
            }
        }
        return Resource.Error(response.message())

    }


    fun searchNews(searchQuery: String, lang: String) =
        viewModelScope.launch {
            safeSearchNewsCall(searchQuery, lang)
        }

    private fun handlingSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { newsResponse ->

                return Resource.Success(newsResponse)
            }
        }
        return Resource.Error(response.message())

    }


    private suspend fun safeBreakingNewsCall(countryCode: String, lang: String) {
        breakingNews.postValue(Resource.Loading())

        try {
            val response =
                newsRepository.getBreakingNews(countryCode, breakingNewsPages, lang = lang)

            breakingNews.postValue(handlingBreakingNewsResponse(response))

        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))

            }
        }
    }

    private suspend fun safeCategoryNewsCall(countryCode: String, lang: String, category: String) {
        categoryNews.postValue(Resource.Loading())

        try {
            if (checkForInternet()) {
                val response = newsRepository.getCategoryNews(
                    countryCode,
                    breakingNewsPages,
                    lang = lang,
                    category = category
                )

                categoryNews.postValue(handlingBreakingNewsResponse(response))
            } else {
                categoryNews.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> categoryNews.postValue(Resource.Error("Network Failure"))
                else -> categoryNews.postValue(Resource.Error("Conversion Error"))

            }
        }
    }

    private suspend fun safeSearchNewsCall(searchQuery: String, lang: String) {
        searchNews.postValue(Resource.Loading())

        try {
            val response = newsRepository.searchNews(searchQuery, searchNewsPages, lang)

            searchNews.postValue(handlingSearchNewsResponse(response))

        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))

            }
        }
    }

    fun checkForInternet(): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager =
            getApplication<NewsApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true

                else -> false
            }
        } else {
            // if the android version is below M
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }


    fun savaArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()
}