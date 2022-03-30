package com.example.smartwindowsapp

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    //private lateinit var auth: FirebaseAuth

    // Firebase Realtime Database
    private val settingsD = Firebase.database.reference.child("Settings")
    private val autoBlindsLevel = settingsD.child("autoBlindsLevel")
    private val autoWindowsLevel = settingsD.child("autoWindowsLevel")
    private val close = settingsD.child("closeOption")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        // Add Fragment to Activity
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        // This enables back button in actionBar
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Updates values from Database
        updateSettings()

        // Below for Dark Mode
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)


    }

    // Inflates hierarchy from XML attribute
    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

    private fun updateSettings(){
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        autoBlindsLevel.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val blindsL = dataSnapshot.getValue<Int>()
                if(blindsL != null){
                    prefs.edit().putInt("auto_blinds_open", blindsL)
                        .apply()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        autoWindowsLevel.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val windowsL = dataSnapshot.getValue<Int>()
                if(windowsL != null){
                    prefs.edit().putInt("auto_windows_open", windowsL)
                        .apply()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        close.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val closeT = dataSnapshot.getValue<Boolean>()
                if(closeT != null){
                    prefs.edit().putBoolean("close_option", closeT)
                        .apply()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        autoBlindsLevel.setValue(prefs.getInt("auto_blinds_open", 100))
        autoWindowsLevel.setValue(prefs.getInt("auto_windows_open", 100))
        close.setValue(prefs.getBoolean("close_option", true))
    }

//    private fun saveUserInfo(){
//        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
//        auth.currentUser?.let {
//            val userName = prefs.getString("name", null)
//            val update = UserProfileChangeRequest.Builder()
//                .setDisplayName(userName)
//                .build()
//
//            GlobalScope.launch(Dispatchers.IO){
//                try{
//                    it.updateProfile(update).await()
//                    withContext(Dispatchers.Main){
//                        Toast.makeText(this@SettingsActivity, "Profile Successfully Updated!",
//                            Toast.LENGTH_SHORT).show()
//                    }
//                }catch (e: Exception){
//                    withContext(Dispatchers.Main){
//                        Toast.makeText(this@SettingsActivity, e.message, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }
}