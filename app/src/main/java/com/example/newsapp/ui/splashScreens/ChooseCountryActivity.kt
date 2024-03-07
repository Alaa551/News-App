package com.example.newsapp.ui.splashScreens

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.newsapp.databinding.ActivityChooseCountryBinding
import com.example.newsapp.data.local.sharedPref.SharedPreferencesManager
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.util.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class ChooseCountryActivity : AppCompatActivity(), OnItemSelectedListener {
    private lateinit var binding: ActivityChooseCountryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseCountryBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setUpAdapter()
        binding.countrySpinner.onItemSelectedListener = this

        binding.next.setOnClickListener {
            if (binding.countrySpinner.selectedItemPosition == 0) {
                Snackbar.make(binding.root, "Please Choose a Country", Snackbar.LENGTH_SHORT).show()



            } else {
                navigateToNews()
            }
        }


    }


    private fun navigateToNews() {
        val intent = Intent(this, ChooseLangActivity::class.java)
        startActivity(intent)
    }

    private fun setUpAdapter() {
        val adapter = ArrayAdapter<String>(
            this,
            R.layout.simple_spinner_dropdown_item,
            Constants.allSupportCountriesOfNews.keys.toList()
        )
        binding.countrySpinner.adapter = adapter
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val selectedCountryFromPosition = p0?.getItemAtPosition(p2) as String

        val selectedCode =
            Constants.allSupportCountriesOfNews[selectedCountryFromPosition] as String

        SharedPreferencesManager.saveCountryOfNews(this, selectedCode)

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }


}