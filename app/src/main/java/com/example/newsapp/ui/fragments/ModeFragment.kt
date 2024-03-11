package com.example.newsapp.ui.fragments



import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.newsapp.databinding.FragmentModeBinding
import com.example.newsapp.data.local.sharedPref.SharedPreferencesManager
import com.example.newsapp.util.Constants


class ModeFragment : Fragment() {
    private lateinit var binding: FragmentModeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentModeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val currentMode = AppCompatDelegate.getDefaultNightMode()

        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.radioButtonDark.isChecked = true
            SharedPreferencesManager.saveThemeMode(requireContext(),"Dark")

        } else {
            binding.radioButtonLight.isChecked = true
            SharedPreferencesManager.saveThemeMode(requireContext(),"Light")

        }

        binding.radioButtonDark.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                SharedPreferencesManager.saveThemeMode(requireContext(),"Dark")

            }
        }
        binding.radioButtonLight.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                SharedPreferencesManager.saveThemeMode(requireContext(),"Light")

            }
        }


    }


}