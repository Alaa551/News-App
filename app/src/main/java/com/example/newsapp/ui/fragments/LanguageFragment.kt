package com.example.newsapp.ui.fragments


import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentLanguageBinding
import com.example.newsapp.databinding.FragmentModeBinding
import com.example.newsapp.data.local.sharedPref.SharedPreferencesManager
import com.example.newsapp.util.Constants
import java.util.*


class LanguageFragment : Fragment(R.layout.fragment_language) {
    private lateinit var binding: FragmentLanguageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val currentLanguage = SharedPreferencesManager.getLanguage(requireContext())

        when (currentLanguage) {
            "en" -> binding.radioButtonEn.isChecked = true
            "ar" -> binding.radioButtonAr.isChecked = true
        }

        binding.radioButtonEn.setOnCheckedChangeListener { _, isCheched ->
            if (isCheched) {
               SharedPreferencesManager.saveLanguage(requireContext(),"en")
                setLocal(requireActivity(), "en")
                refreshAppSetting()
            }
        }
        binding.radioButtonAr.setOnCheckedChangeListener { _, isCheched ->
            if (isCheched) {
                SharedPreferencesManager.saveLanguage(requireContext(),"ar")

                setLocal(requireActivity(), "ar")
                refreshAppSetting()
            }
        }


    }

     fun setLocal(activity: Activity, langCode: String?) {
        val locale = Locale(langCode)
        Locale.setDefault(locale)

        val resources = activity.resources
        val config = resources.configuration

        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)


    }

    private fun refreshAppSetting() {
        requireActivity().recreate()
    }
}
