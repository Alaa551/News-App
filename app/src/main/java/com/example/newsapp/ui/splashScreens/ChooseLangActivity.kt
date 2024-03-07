package com.example.newsapp.ui.splashScreens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.databinding.ActivityChooseLangBinding
import com.example.newsapp.data.local.sharedPref.SharedPreferencesManager
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.util.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class ChooseLangActivity : AppCompatActivity(), OnItemSelectedListener {
    private lateinit var binding: ActivityChooseLangBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseLangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAdapter()




        binding.go.setOnClickListener {
            if(binding.langSpinner.selectedItemPosition== 0){
                Snackbar.make(binding.root, "Please Choose a Language", Snackbar.LENGTH_SHORT).show()
            }
            else {
                navigateToNews()
            }
        }


    }


    private fun navigateToNews() {
        val intent = Intent(this, NewsActivity::class.java)
        startActivity(intent)
    }

    private fun setUpAdapter() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            Constants.allSupportLanguagesOfNews.keys.toList()
        )
        binding.langSpinner.adapter = adapter

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val selectedLanguageFromPosition = p0?.getItemAtPosition(p2) as String

        val selectedCode =
            Constants.allSupportLanguagesOfNews[selectedLanguageFromPosition] as String
        Toast.makeText(this,"lang :${selectedLanguageFromPosition}", Toast.LENGTH_SHORT).show()
        SharedPreferencesManager.saveLanguageOfNews(this, selectedCode)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}