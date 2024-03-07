package com.example.newsapp.util

sealed class Resource<T>(
    val status: Status,
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(Status.SUCCESS, data)
    class Loading<T> : Resource<T>(Status.LOADING)
    class Error<T>(message: String) : Resource<T>(Status.ERROR, message = message)
}



