package com.example.smartwindowsapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.smartwindowsapp.R
import kotlinx.android.synthetic.main.fragment_manual.*

class ManualFragment : Fragment(R.layout.fragment_manual){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Functional code needs to be run in onViewCreated under fragments hmm...
        var mOnFlag = false
        var blindsLevel: Int
        var windowLevel: Int
        val percent = "%"

        blinds_seekbar.min = 0
        blinds_seekbar.max = 10

        window_seekbar.min = 0
        window_seekbar.max = 10

        val mPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val mEditor = mPref.edit()

        if(!mOnFlag){
            mOnFlag = true
            blindsLevel = 10*mPref.getInt("blindsval", -1)
            windowLevel = 10*mPref.getInt("windowsval", -1)
            // To make sure app doesn't show default values when app is launched
            if(blindsLevel != -10)
                (blindsLevel.toString() + percent).also { blinds_val.text = it }
            if(windowLevel != -10)
                (windowLevel.toString() + percent).also { window_val.text = it }
            // Sets seekbar to obtained Pref values
            blinds_seekbar.progress = mPref.getInt("blindsval", -1)
            window_seekbar.progress = mPref.getInt("windowsval", -1)
        }

        blinds_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                blindsLevel = progress*10
                (blindsLevel.toString() + percent).also { blinds_val.text = it }
                mEditor.apply{
                    putInt("blindsval", progress)
                    apply()
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        window_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                windowLevel = progress*10
                (windowLevel.toString() + percent).also { window_val.text = it }
                mEditor.apply{
                    putInt("windowsval", progress)
                    apply()
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }
}
