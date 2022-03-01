package com.example.smartwindowsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.smartwindowsapp.fragments.AutomaticFragment
import com.example.smartwindowsapp.fragments.ManualFragment
import com.example.smartwindowsapp.fragments.SmartFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class MainActivity : AppCompatActivity() {

    // Firebase Realtime
    private val mainD = Firebase.database.reference.child("SelectedMode")
    // Firebase Auth
    private lateinit var auth: FirebaseAuth

    private val autoFragment = AutomaticFragment()
    private val smartFragment = SmartFragment()
    private val manualFragment = ManualFragment()

    //private lateinit var binding: ActivityMainBinding //What is this?

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.upper_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        auth = FirebaseAuth.getInstance()
        when (item.itemId) {
            R.id.logout -> {
                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Successfully signed out!", Toast.LENGTH_SHORT).show()
            }
            R.id.action_settings -> {
                // Start settings menu
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.action_location ->{
                // Start location settings
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_SmartWindowsApp)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        // If not loggedin, launch LoginActivity
        if(auth.currentUser != null) {
            var currentFragment: Int

            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            // Loads current fragment from value retrieved from Firebase
            mainD.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot){
                    val mainD = dataSnapshot.getValue<Int>()
                    if (mainD != null) {
                        // Save data to local
                        currentFragment = mainD
                        when(currentFragment){
                            1 -> {
                                setCurrentFragment(smartFragment)
                                bottomNavigationView.selectedItemId = R.id.smart
                            }
                            2 -> {
                                setCurrentFragment(autoFragment)
                                bottomNavigationView.selectedItemId = R.id.automatic
                            }
                            3 -> {
                                setCurrentFragment(manualFragment)
                                bottomNavigationView.selectedItemId = R.id.manual
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })

            // Sets fragment to appropriate mode based on the mode clicked in bottomNavBar
            bottomNavigationView.setOnNavigationItemSelectedListener{
                when(it.itemId){// Maps bottom navigation to modes
                    R.id.automatic -> setCurrentFragment(autoFragment)
                    R.id.manual -> setCurrentFragment(manualFragment)
                    R.id.smart -> setCurrentFragment(smartFragment)
                }
                true
            }
            mySettings() // Runs function to retrieve setting values
        }
        else{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun mySettings() { // Function to get settings values
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val signature = prefs.getString("name", "")
        val owner = prefs.getBoolean("primary_owner", false)
        val numUsers = prefs.getInt("user_number", 1)
        val security = prefs.getBoolean("close_option", false)

        //println(prefs)
        println(signature)
        println(owner)
        println(numUsers)
        println(security)
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            when(fragment){
                smartFragment -> mainD.setValue(1)
                autoFragment -> mainD.setValue(2)
                manualFragment -> mainD.setValue(3)
            }
            setReorderingAllowed(true)
            //setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
//            if(currentFrag == autoFragment && fragment == smartFragment || currentFrag == manualFragment)
//                setCustomAnimations(R.anim.slide_in_back, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out_back)
            replace(R.id.fragment_c, fragment)
            addToBackStack(null) // Make back button work as intended
            commit()
        }
}