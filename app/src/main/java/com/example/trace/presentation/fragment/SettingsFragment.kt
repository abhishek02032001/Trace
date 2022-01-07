package com.example.trace.presentation.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.trace.R
import com.example.trace.common.Constants.KEY_NAME
import com.example.trace.common.Constants.KEY_WEIGHT
import com.example.trace.presentation.viewModels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var sharedPrefs:SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFields()
        btnApplyChanges.setOnClickListener{
            val success = applyChanges()
            if(success) {
                Snackbar.make(view, "Updated User", Snackbar.LENGTH_LONG).show()
                loadFields()
            }
            else{
                Snackbar.make(view, "All fields are required", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun loadFields(){
        val name = sharedPrefs.getString(KEY_NAME, "")
        val weight = sharedPrefs.getFloat(KEY_WEIGHT, 80f)
        etName.setText(name)
        etWeight.setText(weight.toString())
    }

    private fun applyChanges() : Boolean{
        val name = etName.text.toString()
        val weight = etWeight.text.toString()

        if(name.isEmpty() || weight.isEmpty())
            return false
        sharedPrefs.edit()
            .putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .apply()
        return true
    }

}