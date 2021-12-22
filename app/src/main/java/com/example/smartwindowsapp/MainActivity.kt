package com.example.smartwindowsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val autoFragment = AutomaticFragment()
        val smartFragment = SmartFragment()
        val manualFragment = ManualFragment()

        setCurrentFragment(smartFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.automatic -> setCurrentFragment(autoFragment)
                R.id.manual -> setCurrentFragment(manualFragment)
                R.id.smart -> setCurrentFragment(smartFragment)
            }
            true
        }
    }
    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_c, fragment)
            addToBackStack(null) // Make back button work as intended
            commit()
        }
}