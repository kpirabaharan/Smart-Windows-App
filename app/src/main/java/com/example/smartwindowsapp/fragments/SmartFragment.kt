package com.example.smartwindowsapp.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import com.example.smartwindowsapp.R
import kotlinx.android.synthetic.main.fragment_smart.*

class SmartFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_smart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        temperatureChange()
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
        var cOrF = "°C"
        var temp: Int

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
        }

        temp_unit.setOnValueChangedListener { numberPicker, oldVal, newVal ->
            if (temp_unit.value == 0)
            {
                temp_input.minValue = 15
                temp_input.maxValue = 30
                temp_input.value = 15
            }
            else
            {
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
        }
    }
}