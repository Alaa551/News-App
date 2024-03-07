package com.example.newsapp.util

class Constants {
    companion object{
        const val LANGUAGE="language_shared_file"
        const val SH_PER_FILE_NAME="mode_shared_file"
        const val MODE="mode"
        const val LANGUAGE_OF_NEWS="lang_of_news"
        const val COUNTRY_OF_NEWS="country_of_news"
        const val API_KEY="de0269d7c381454fc843e2d2d8091d04"

        const val BASE_URL= "https://gnews.io/api/v4/"
        const val QUERY_PAGE_SIZE= 10
        val allSupportLanguagesOfNews = mapOf(
            "languages: " to "",
            "Arabic" to "ar",
            "Chinese" to "zh",
            "Dutch" to "nl",
            "English" to "en",
            "French" to "fr",
            "German" to "de",
            "Japanese" to "ja",
            "Spanish" to "es",
            "Hindi" to "hi",
            "Italian" to "it"
        )
        val allSupportCountriesOfNews = mapOf(
            "countries: " to "",
            "Egypt" to "eg",
            "Australia" to "au",
            "Brazil" to "br",
            "Canada" to "ca",
            "China" to "cn",
            "France" to "fr",
            "Germany" to "de",
            "Hong Kong" to "hk",
            "India" to "in",
            "Ireland" to "ie",
            "Italy" to "it",
            "Japan" to "jp",
            "Netherlands" to "nl",
            "Norway" to "no",
            "Pakistan" to "pk",
            "Portugal" to "pt",
            "Romania" to "ro",
            "Russian Federation" to "ru",
            "Singapore" to "sg",
            "Spain" to "es",
            "Sweden" to "se",
            "United Kingdom" to "gb",
            "United States" to "us"
        )

    }
}