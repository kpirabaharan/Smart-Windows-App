package com.example.smartwindowsapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smartwindowsapp.R
import com.example.smartwindowsapp.classes.SmartValues
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_smart.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.lang.StringBuilder

class SmartFragment : Fragment(R.layout.fragment_smart){
    // Realtime
    private val sD = Firebase.database
    private val tempD = sD.getReference("temp")
    private val unitD = sD.getReference("unit")

    var temp = 0;
    var cOrF = "°C"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        temperatureChange()
//        btnSave.setOnClickListener {
//            val temperature = temp
//            val tempUnit = cOrF
//            val smartValues = SmartValues(temperature, tempUnit)
//            saveTemp(smartValues)
//        }
    }

    private fun temperatureChange(){
        desired_temp_text.text = "Set Desired Temperature"
        // Temperature Units, should move to settings
        val unit = arrayOf("°C", "°F")
        var sBool = false
        temp_input.minValue = 15
        temp_input.maxValue = 30
        temp_unit.displayedValues = unit
        temp_unit.minValue = 0
        temp_unit.maxValue = unit.size-1

        // Initialize to last set temperature, obtained from Realtime Database, only on starting fragment
        tempD.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tempD = dataSnapshot.getValue<Int>()
                if (tempD != null) {
                    temp = tempD
                    if(temp in 59..86) {
                        temp_input.minValue = 59
                        temp_input.maxValue = 86
                        temp_unit.value = 1
                        cOrF = "°F"
                    }
                    temp_input.value = temp
                }
                desiredTempText()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Failed to read value.", Toast.LENGTH_LONG).show()
            }
        })
        // Temperature Selection
        temp_input.setOnValueChangedListener { numberPicker, oldVal, newVal ->
            temp = newVal
            desiredTempText()
            val newSmartValues = getNewSmartValues()
            updateSmartValues(newSmartValues)
        }

        temp_unit.setOnValueChangedListener { numberPicker, oldVal, newVal ->
            // If unit is C change values to 15...30 and default value of 15
            if (temp_unit.value == 0) {
                temp_input.minValue = 15
                temp_input.maxValue = 30
                temp_input.value = 15
            }
            // If unit is F change values to 59...86 and default value of 59
            else {
                temp_input.minValue = 59
                temp_input.maxValue = 86
            }
            cOrF = unit[newVal]
            if(temp_unit.value == 0)
                temp = 15
            else
                temp = 59
            desiredTempText()
            val newSmartValues = getNewSmartValues()
            updateSmartValues(newSmartValues)
        }
    }

    // Saves temperature for the first time in fireStore
    private fun saveTemp(smartValues: SmartValues) = CoroutineScope(Dispatchers.IO).launch{
        // Try catch b/c we are uploading to a server
        try {
            // Push to Realtime
            tempD.setValue(smartValues.temp)
            unitD.setValue(smartValues.unit)
            withContext(Dispatchers.Main){
                Toast.makeText(activity, "Successfully saved temperature.", Toast.LENGTH_LONG).show()
            }
        }catch(e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateSmartValues(newSmartValues: SmartValues) = CoroutineScope(Dispatchers.IO).launch {
        // Value can be anything, just update database
        // Realtime
        try {
            tempD.setValue(newSmartValues.temp)
            unitD.setValue(newSmartValues.unit)
        }catch (e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun getNewSmartValues(): SmartValues{
        val temp = temp
        val unit = cOrF
        return SmartValues(temp, unit)
    }

    private fun desiredTempText(){
        desired_temp_text.text = "Desired Temperature: "
        desired_temp_text.append(temp.toString())
        desired_temp_text.append(cOrF)
    }
}