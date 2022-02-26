package com.example.smartwindowsapp.fragments

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.smartwindowsapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_manual.*

class ManualFragment : Fragment(R.layout.fragment_manual){
    // Realtime Database
    private val mD = Firebase.database.reference.child("Manual")
    private val blindsValD = mD.child("blindsVal")
    private val windowsValD = mD.child("windowsVal")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Functional code needs to be run in onViewCreated under fragments hmm...
        var blindsLevel: Int
        var windowsLevel: Int
        val percent = "%"

        blinds_seekbar.min = 0
        blinds_seekbar.max = 10
        window_seekbar.min = 0
        window_seekbar.max = 10

        blindsValD.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val blindsData = dataSnapshot.getValue<Int>()
                if (blindsData != null) {
                    blindsLevel = 10*blindsData
                    (blindsLevel.toString() + percent).also { blinds_val.text = it }
                    blinds_seekbar.progress = blindsData
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        windowsValD.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val windowsData = dataSnapshot.getValue<Int>()
                if (windowsData != null) {
                    windowsLevel = 10*windowsData
                    (windowsLevel.toString() + percent).also { window_val.text = it }
                    window_seekbar.progress = windowsData
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        blinds_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                blindsLevel = progress*10
                (blindsLevel.toString() + percent).also { blinds_val.text = it }
                // Update realtime Database
                blindsValD.setValue(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        window_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                windowsLevel = progress*10
                (windowsLevel.toString() + percent).also { window_val.text = it }
                // Update realtime Database
                windowsValD.setValue(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }
}
