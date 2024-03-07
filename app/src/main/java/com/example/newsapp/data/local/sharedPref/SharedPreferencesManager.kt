package com.example.newsapp.data.local.sharedPref

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.example.newsapp.util.Constants


class SharedPreferencesManager {
   companion object {

       private fun getSharedPreferences(context: Context): SharedPreferences {
           return context.getSharedPreferences(Constants.SH_PER_FILE_NAME, Context.MODE_PRIVATE)
       }

       fun saveThemeMode(context: Context, themeMode: String) {
           val editor = getSharedPreferences(context).edit()
           editor.putString(Constants.MODE, themeMode)
           editor.apply()
       }

       fun getThemeMode(context: Context): String {
           return getSharedPreferences(context).getString(Constants.MODE, "Dark")
               ?: "Dark"
       }


       fun saveLanguage(context: Context, lang: String) {
           val editor = getSharedPreferences(context).edit()
           editor.putString(Constants.LANGUAGE, lang)
           editor.apply()
       }

       fun getLanguage(context: Context): String {
           return getSharedPreferences(context).getString(Constants.LANGUAGE, "en")
               ?: "en"
       }

       fun saveLanguageOfNews(context: Context, lang: String) {
           val editor = getSharedPreferences(context).edit()
           editor.putString(Constants.LANGUAGE_OF_NEWS, lang)
           editor.apply()
       }

       fun getLanguageOfNews(context: Context): String {
           return getSharedPreferences(context).getString(Constants.LANGUAGE_OF_NEWS, "en")
               ?: "en"
       }

       fun saveCountryOfNews(context: Context, lang: String) {
           val editor = getSharedPreferences(context).edit()
           editor.putString(Constants.COUNTRY_OF_NEWS, lang)
           editor.apply()
       }

       fun getCountryOfNews(context: Context): String {
           return getSharedPreferences(context).getString(Constants.COUNTRY_OF_NEWS, "us")
               ?: "us"
       }

   }




}