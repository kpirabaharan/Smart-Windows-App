package com.example.smartwindowsapp

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class SettingsActivity : AppCompatActivity(), // Next line needed for onSharedPreferenceChanged
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        // Sets Action Bar title to Settings
//        actionBar?.title = "Settings"
//        supportActionBar?.title = "Settings"

        // Assume this is used to save settings?
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.settings,
                SettingsFragment()).commit()
        }
        // This enables back button in actionBar
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Below for Dark Mode
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
//        if (key == "dark_mode")
//        {
//            val prefs = PreferenceManager.getDefaultSharedPreferences(this)
//            val dMode = prefs.getBoolean("dark_mode", true)
//            if(dMode)
//            {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            }
//            else
//            {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            }
//        }
        }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }
}