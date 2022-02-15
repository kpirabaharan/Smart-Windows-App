package com.example.smartwindowsapp.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.smartwindowsapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_automatic.*
import java.util.*


class AutomaticFragment : Fragment(R.layout.fragment_automatic){

    // Realtime Database
    private val aD = Firebase.database.reference.child("Automatic")
    // Blinds and Windows paths under Automatic
    private val aBlindsD = aD.child("Blinds")
    private val aWindowsD = aD.child("Windows")
    // Variables for each hour and minute
    private val bOpenHD = aBlindsD.child("bOpenHour")
    private val bOpenMD = aBlindsD.child("bOpenMinute")
    private val bCloseHD = aBlindsD.child("bCloseHour")
    private val bCloseMD = aBlindsD.child("bCloseMinute")

    private val wOpenHD = aWindowsD.child("wOpenHour")
    private val wOpenMD = aWindowsD.child("wOpenMinute")
    private val wCloseHD = aWindowsD.child("wCloseHour")
    private val wCloseMD = aWindowsD.child("wCloseMinute")

    var hour = 0
    var minute = 0

    // Local values
    var bOpenSavedHour = 0
    var bOpenSavedMinute = 0
    var bCloseSavedHour = 0
    var bCloseSavedMinute = 0
    var wOpenSavedHour = 0
    var wOpenSavedMinute = 0
    var wCloseSavedHour = 0
    var wCloseSavedMinute = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bOpen = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            bOpenSavedHour = hourOfDay
            bOpenSavedMinute = minute
            // Save Values to Realtime Database
            bOpenHD.setValue(hourOfDay)
            bOpenMD.setValue(minute)
            // Set appropriate text
            setTime(openBText, hourOfDay, minute)
        }
        val bClose = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            bCloseSavedHour = hourOfDay
            bCloseSavedMinute = minute
            bCloseHD.setValue(hourOfDay)
            bCloseMD.setValue(minute)
            setTime(closeBText, hourOfDay, minute)
        }
        val wOpen = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            wOpenSavedHour = hourOfDay
            wOpenSavedMinute  = minute
            wOpenHD.setValue(hourOfDay)
            wOpenMD.setValue(minute)
            setTime(openWText, hourOfDay, minute)
        }
        val wClose = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            wCloseSavedHour = hourOfDay
            wCloseSavedMinute = minute
            wCloseHD.setValue(hourOfDay)
            wCloseMD.setValue(minute)
            setTime(closeWText, hourOfDay, minute)
        }

        // Retrieve data on launch
        bOpenHD.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val bOpenHD = dataSnapshot.getValue<Int>()
                if (bOpenHD != null) {
                    // Save data to local
                    bOpenSavedHour = bOpenHD
                    // Set time after retrieval
                    setTime(openBText, bOpenSavedHour, bOpenSavedMinute)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        bOpenMD.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val bOpenMD = dataSnapshot.getValue<Int>()
                if (bOpenMD != null) {
                    bOpenSavedMinute = bOpenMD
                    setTime(openBText, bOpenSavedHour, bOpenSavedMinute)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        bCloseHD.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val bCloseHD = dataSnapshot.getValue<Int>()
                if (bCloseHD != null) {
                    bCloseSavedHour = bCloseHD
                    setTime(closeBText, bCloseSavedHour, bCloseSavedMinute)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        bCloseMD.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val bCloseMD = dataSnapshot.getValue<Int>()
                if (bCloseMD != null) {
                    bCloseSavedMinute = bCloseMD
                    setTime(closeBText, bCloseSavedHour, bCloseSavedMinute)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        wOpenHD.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val wOpenHD = dataSnapshot.getValue<Int>()
                if (wOpenHD != null) {
                    wOpenSavedHour = wOpenHD
                    setTime(openWText, wOpenSavedHour, wOpenSavedMinute)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        wOpenMD.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val wOpenMD = dataSnapshot.getValue<Int>()
                if (wOpenMD != null) {
                    wOpenSavedMinute = wOpenMD
                    setTime(openWText, wOpenSavedHour, wOpenSavedMinute)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        wCloseHD.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val wCloseHD = dataSnapshot.getValue<Int>()
                if (wCloseHD != null) {
                    wOpenSavedHour = wCloseHD
                    setTime(closeWText, wCloseSavedHour, wCloseSavedMinute)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        wCloseMD.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val wCloseMD = dataSnapshot.getValue<Int>()
                if (wCloseMD != null) {
                    wOpenSavedMinute = wCloseMD
                    setTime(closeWText, wCloseSavedHour, wCloseSavedMinute)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        // When button clicked launches TimePickerDialog with value below
        btn_openBlinds.setOnClickListener{
            pickTime(bOpen, bOpenSavedHour, bOpenSavedMinute)
        }
        btn_closeBlinds.setOnClickListener {
            pickTime(bClose, bCloseSavedHour, bCloseSavedMinute)
        }
        btn_openWindows.setOnClickListener{
            pickTime(wOpen, wOpenSavedHour, wOpenSavedMinute)
        }
        btn_closeWindows.setOnClickListener{
            pickTime(wClose, wCloseSavedHour, wCloseSavedMinute)
        }
    }
    // When an instance of a TimePicker dialog is clicked
    private fun pickTime(picker: TimePickerDialog.OnTimeSetListener, hour: Int? = null, minute: Int? = null) {
        if(hour == null && minute == null)
            getTime()
        if (hour != null && minute != null) {
            TimePickerDialog(activity, picker, hour, minute, true).show()
        }
    }
    // Only used when app is first launched, gets calendar hour and minute
    private fun getTime() {
        val cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }
    // Text setting function
    private fun setTime(textV: TextView, hourOfDay: Int, minute: Int) {
        if(minute in 0..9)
            textV.text = "$hourOfDay:0$minute"
        else
            textV.text = "$hourOfDay:$minute"
    }

}