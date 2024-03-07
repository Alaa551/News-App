package com.example.newsapp.ui.fragments

// BaseFragment.kt
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.data.local.ArticleDatabase
import com.example.newsapp.repository.NewsRepository

abstract class BaseFragment<VM : ViewModel>() : Fragment() {

    abstract fun getViewModelClass(): Class<VM>

    abstract fun getViewModelFactory(): ViewModelProvider.Factory

    lateinit var viewModel: VM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, getViewModelFactory())[getViewModelClass()]
    }
}
