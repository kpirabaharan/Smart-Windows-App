package com.example.smartwindowsapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import com.example.smartwindowsapp.databinding.ActivityMainBinding
import com.example.smartwindowsapp.fragments.AutomaticFragment
import com.example.smartwindowsapp.fragments.ManualFragment
import com.example.smartwindowsapp.fragments.SmartFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    //private lateinit var binding: ActivityMainBinding //What is this?

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                // Start settings menu
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_SmartWindowsApp)
        setContentView(R.layout.activity_main)

        //val autoPref = getSharedPreferences("myPref", Context.MODE_PRIVATE)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val autoFragment = AutomaticFragment()
        val smartFragment = SmartFragment()
        val manualFragment = ManualFragment()

        var currentFragment: Fragment

        setCurrentFragment(smartFragment) // Sets current fragment, default fragment smart

        // Sets fragment to appropriate mode based on the mode clicked in bottomNavBar
        bottomNavigationView.setOnNavigationItemSelectedListener{
            when(it.itemId){// Maps bottom navigation to modes
                R.id.automatic -> { setCurrentFragment(autoFragment) }
                R.id.manual -> { setCurrentFragment(manualFragment) }
                R.id.smart -> { setCurrentFragment(smartFragment) }
            }
            true
        }
        mySettings() // Runs function to retrieve setting values
    }

    private fun mySettings() { // Function to get settings values
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val signature = prefs.getString("name", "")
        val owner = prefs.getBoolean("primary_owner", false)
        val numUsers = prefs.getInt("user_number", 1)
        val security = prefs.getBoolean("close_option", false)
        val dMode = prefs.getBoolean("dark_mode", false)

        //println(prefs)
        println(signature)
        println(owner)
        println(numUsers)
        println(security)
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
//            setCustomAnimations(
//                enter = R.anim.slide_in,
//                exit = R.anim.fade_out,
//                popEnter = R.anim.fade_in,
//                popExit = R.anim.slide_out
//            )
            replace(R.id.fragment_c, fragment)
            addToBackStack(null) // Make back button work as intended
            commit()
        }
}