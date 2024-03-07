package com.example.newsapp.ui.splashScreens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.newsapp.databinding.ActivityWelcomeScreenBinding
import com.example.newsapp.data.local.sharedPref.SharedPreferencesManager
import com.example.newsapp.ui.fragments.LanguageFragment
import com.example.newsapp.util.Constants
import kotlinx.coroutines.*

class WelcomeScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeScreenBinding
    private var splashJob: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeAppTheme()



        // set up animation
        setUPAnim()


        binding.start.setOnClickListener {
            navigateToNextSplash()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        splashJob?.cancel()
    }

    private fun navigateToNextSplash() {
        val intent = Intent(this, ChooseCountryActivity::class.java)
        startActivity(intent)
    }


    private fun initializeAppTheme() {
        val mode = SharedPreferencesManager.getThemeMode(this)
        if (mode == "Dark") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

    }



    private fun setUPAnim(){
        binding.animationView.playAnimation()

        splashJob = CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
            binding.animationView.cancelAnimation()

        }
    }
}