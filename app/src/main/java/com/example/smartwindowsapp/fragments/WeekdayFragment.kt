package com.example.smartwindowsapp.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartwindowsapp.R
import kotlinx.android.synthetic.main.fragment_smart.*
import kotlinx.android.synthetic.main.fragment_weekday.*
import java.text.SimpleDateFormat
import java.util.*


class WeekdayFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weekday, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        pickOpenTime.setOnClickListener {
//            val cal = Calendar.getInstance()
//            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
//                cal.set(Calendar.HOUR_OF_DAY, hour)
//                cal.set(Calendar.MINUTE, minute)
//                // set time to textview
//                timeOpen.text = SimpleDateFormat("HH:mm").format(cal.time)
//            }
//            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()

        var hour = 0
        var min = 0

        time_hour.minValue = 0
        time_hour.maxValue = 23
        time_minute.minValue = 0
        time_minute.maxValue = 59

        time_hour.setOnValueChangedListener { picker, oldVal, newVal ->
            hour = newVal
        }
        time_minute.setOnValueChangedListener { picker, oldVal, newVal ->
            min = newVal


        }

    }
}