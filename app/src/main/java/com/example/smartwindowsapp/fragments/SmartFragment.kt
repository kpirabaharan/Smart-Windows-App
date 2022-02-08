package com.example.smartwindowsapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smartwindowsapp.R
import com.example.smartwindowsapp.classes.SmartValues
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_smart.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class SmartFragment : Fragment(R.layout.fragment_smart){

    private val smartCollectionRef = Firebase.firestore.collection("smartValues")

    var temp = 0;
    var cOrF = "°C"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        temperatureChange()
        btnSave.setOnClickListener {
//            print("Old Temp: $oldTemp")
//            print("New Temp: $temp")
//            print("Old Unit: $oldcOrF")
//            print("New Unit: $cOrF")
            val temperature = temp
            val tempUnit = cOrF
            val smartValues = SmartValues(temperature, tempUnit)
            saveTemp(smartValues)
        }
    }

    // Saves temperature for the first time in fireStore
    private fun saveTemp(smartValues: SmartValues) = CoroutineScope(Dispatchers.IO).launch{
        // Try catch b/c we are uploading to a server
        // Simple toasts for confirmation
        try {
            smartCollectionRef.add(smartValues).await()
            withContext(Dispatchers.Main){
                Toast.makeText(activity, "Successfully saved temperature.", Toast.LENGTH_LONG).show()
            }
        }catch(e: Exception){
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

    private fun updateSmartValues(newSmartValues: SmartValues) = CoroutineScope(Dispatchers.IO).launch {
        // Value can be anything, just update firebase
        val smartValuesQuery = smartCollectionRef
            .get()
            .await()
        for (document in smartValuesQuery){
            try {
                smartCollectionRef.document(document.id).update("temp", newSmartValues.temp).await()
                smartCollectionRef.document(document.id).update("unit", newSmartValues.unit).await()
//                withContext(Dispatchers.Main){
//                    Toast.makeText(activity, "Changed", Toast.LENGTH_SHORT).show()
//                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun temperatureChange(){
        desired_temp_text.text = "Set Desired Temperature"
        // Temperature Units, should move to settings
        val unit = arrayOf("°C", "°F")
        var sOnFlag = false
        temp_input.minValue = 15
        temp_input.maxValue = 30
        temp_unit.displayedValues = unit
        temp_unit.minValue = 0
        temp_unit.maxValue = unit.size-1

        val sPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val sEditor = sPref.edit()

        // Initialize to last set temperature, obtained from Preference
        if(!sOnFlag){
            sOnFlag = true
            temp = sPref.getInt("temperature", -1)
            cOrF = unit[sPref.getInt("unit", 0)]
            if(temp != -1){
                desired_temp_text.text = "Desired Temperature: "
                desired_temp_text.append(temp.toString())
                if(temp in 59..86)
                {
                    temp_unit.value = sPref.getInt("unit", 0)
                    temp_input.minValue = 59
                    temp_input.maxValue = 86
                }
                    temp_input.value = temp
            }
        }

        // Temperature Selection
        temp_input.setOnValueChangedListener { numberPicker, oldVal, newVal ->
            temp = newVal
            desired_temp_text.text = "Desired Temperature: "
            desired_temp_text.append(temp.toString())
            desired_temp_text.append(cOrF)
            // Save temperature to Pref
            sEditor.apply{
                putInt("temperature", temp)
                apply()
            }
            val newSmartValues = getNewSmartValues()
            updateSmartValues(newSmartValues)
        }

        temp_unit.setOnValueChangedListener { numberPicker, oldVal, newVal ->
            if (temp_unit.value == 0) {
                temp_input.minValue = 15
                temp_input.maxValue = 30
                temp_input.value = 15
            }
            else {
                temp_input.minValue = 59
                temp_input.maxValue = 86
            }
            cOrF = unit[newVal]
            if(temp_unit.value == 0)
                temp = 15
            else
                temp = 59
            desired_temp_text.text = "Desired Temperature: "
            desired_temp_text.append(temp.toString())
            desired_temp_text.append(cOrF)
            // Save temperature and unit to Pref
            sEditor.apply{
                putInt("temperature", temp)
                putInt("unit", newVal)
                apply()
            }
            val newSmartValues = getNewSmartValues()
            updateSmartValues(newSmartValues)
        }
    }
}