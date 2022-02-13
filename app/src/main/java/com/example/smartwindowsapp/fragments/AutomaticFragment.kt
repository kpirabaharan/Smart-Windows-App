package com.example.smartwindowsapp.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.example.smartwindowsapp.R
import kotlinx.android.synthetic.main.fragment_automatic.*
import java.util.*


class AutomaticFragment : Fragment(R.layout.fragment_automatic){

    var hour = 0
    var minute = 0
    var savedHour = 0
    var savedMinute = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var bOpen = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            if(minute in 0..9)
                openBText.text = "$hourOfDay:0$minute"
            else
                openBText.text = "$hourOfDay:$minute"
        }
        var bClose = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            if(minute in 0..9)
                closeBText.text = "$hourOfDay:0$minute"
            else
                closeBText.text = "$hourOfDay:$minute"
        }
        var wOpen = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            if(minute in 0..9)
                openWText.text = "$hourOfDay:0$minute"
            else
                openWText.text = "$hourOfDay:$minute"
        }
        var wClose = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            if(minute in 0..9)
                closeWText.text = "$hourOfDay:0$minute"
            else
                closeWText.text = "$hourOfDay:$minute"
        }

        btn_openBlinds.setOnClickListener{
            pickTime(bOpen)
        }
        btn_closeBlinds.setOnClickListener {
            pickTime(bClose)
        }
        btn_openWindows.setOnClickListener{
            pickTime(wOpen)
        }
        btn_closeWindows.setOnClickListener{
            pickTime(wClose)
        }
    }

    private fun pickTime(picker: TimePickerDialog.OnTimeSetListener) {
        getTime()
        TimePickerDialog(activity, picker, hour, minute, true).show()
    }

    private fun getTime() {
        val cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

}