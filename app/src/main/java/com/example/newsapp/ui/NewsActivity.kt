package com.example.newsapp.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.R
import com.example.newsapp.data.local.ArticleDatabase
import com.example.newsapp.databinding.ActivityNewsBinding
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.data.local.sharedPref.SharedPreferencesManager
import com.example.newsapp.ui.fragments.LanguageFragment
import com.example.newsapp.util.Constants
import com.example.newsapp.viewModel.NewViewModelProviderFactory
import com.example.newsapp.viewModel.NewsViewModel


class NewsActivity : AppCompatActivity() {
    //    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var binding: ActivityNewsBinding
    private lateinit var newsViewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeAppLanguage()

        // setup view model

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val newViewModelProviderFactory = NewViewModelProviderFactory(
            application,
            newsRepository,
            SharedPreferencesManager.getLanguageOfNews(this),
            SharedPreferencesManager.getCountryOfNews(this)
        )
        // It's responsible for managing ViewModels and their lifecycles.
        //  It provides methods to retrieve ViewModel instances associated with a particular lifecycle scope,
        //  such as an Activity or Fragment.
        newsViewModel =
            ViewModelProvider(this, newViewModelProviderFactory)[NewsViewModel::class.java]
// this -> It serves as the lifecycle owner for the ViewModel.
// The ViewModel's lifecycle is tied to the lifecycle of the owner (Activity or Fragment),
// ensuring that ViewModel is destroyed when the owner is destroyed to prevent memory leaks.

        // set up bottom navbar
        val navHostController =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostController.navController
        binding.bottomNav.setupWithNavController(navController)


    }
    private fun initializeAppLanguage() {


        val language = SharedPreferencesManager.getLanguage(this)

        val languageSettingActivity = LanguageFragment()
        languageSettingActivity.setLocal(this, language)
    }



}