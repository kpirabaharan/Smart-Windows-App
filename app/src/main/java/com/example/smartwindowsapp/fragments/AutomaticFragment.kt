package com.example.smartwindowsapp.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.smartwindowsapp.R
import kotlinx.android.synthetic.main.fragment_automatic.*


class AutomaticFragment : Fragment(R.layout.fragment_automatic){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(savedInstanceState == null) {
            super.onViewCreated(view, savedInstanceState)
            timeSelection()
        }
        if(savedInstanceState != null)
        {
            onViewStateRestored(savedInstanceState)
        }
    }

    private fun timeSelection(){
        var blindsOpenTime: Int
        var blindsCloseTime: Int
        var windowOpenTime: Int
        var windowCloseTime: Int

        blinds_open_hour.minValue = 0
        blinds_close_hour.minValue = 0
        window_close_hour.minValue = 0
        window_open_hour.minValue = 0
        blinds_open_hour.maxValue = 23
        blinds_close_hour.maxValue = 23
        window_close_hour.maxValue = 23
        window_open_hour.maxValue = 23

        blinds_open_min.minValue = 0
        blinds_close_min.minValue = 0
        window_close_min.minValue = 0
        window_open_min.minValue = 0
        blinds_open_min.maxValue = 59
        blinds_close_min.maxValue = 59
        window_close_min.maxValue = 59
        window_open_min.maxValue = 59
    }
}