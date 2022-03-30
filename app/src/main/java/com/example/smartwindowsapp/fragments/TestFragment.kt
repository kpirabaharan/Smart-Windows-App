package com.example.smartwindowsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.smartwindowsapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_test.*

class TestFragment : Fragment(R.layout.fragment_test) {

    private val tD = Firebase.database.reference.child("Test")
    private val lightD = tD.child("light")
    private val rainD = tD.child("rain")
    private val insideTempD = tD.child("insideTemp")
    private val outsideTempD = tD.child("outsideTemp")

    var light = 0
    var rain = 0
    var insideTemp = ""
    var outsideTemp = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        testChange()
    }

    private fun testChange() {
        // Get Light Val
        lightD.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val lightData = dataSnapshot.getValue<Int>()
                if(lightData != null){
                    light = lightData
                    if(light == 1)
                        toggle_light.isChecked = true
                    if(light == 0)
                        toggle_light.isChecked = false
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        // Set Light ToggleButton
        toggle_light.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                light = 1
                lightD.setValue(1)
            }
            else{
                light = 0
                lightD.setValue(0)
            }
        }
        // Get Rain Val
        rainD.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val rainData = dataSnapshot.getValue<Int>()
                if(rainData != null){
                    rain = rainData
                    if(rain == 1)
                        toggle_rain.isChecked = true
                    if(rain == 0)
                        toggle_rain.isChecked = false
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        // Set Rain ToggleButton
        toggle_rain.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                rain = 1
                rainD.setValue(1)
            }
            else{
                rain = 0
                rainD.setValue(0)
            }
        }
        // Get Inside Temp
        insideTempD.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val insideTempData = dataSnapshot.getValue<Int>()
                if(insideTempData != null){
                    editTextInsideTemp.setText(insideTempData.toString())
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        // Set Inside Temp
        btn_insideTempSet.setOnClickListener {
            insideTemp = editTextInsideTemp.text.toString()
            insideTempD.setValue(Integer.parseInt(insideTemp))
        }
        // Get Outside Temp
        outsideTempD.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val outsideTempData = dataSnapshot.getValue<Int>()
                if(outsideTempData != null){
                    editTextOutsideTemp.setText(outsideTempData.toString())
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        // Set Outside Temp
        btn_outsideTempSet.setOnClickListener {
            outsideTemp = editTextOutsideTemp.text.toString()
            outsideTempD.setValue(Integer.parseInt(outsideTemp))
        }
    }
}