package com.example.smartwindowsapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private val TAG = "GeofenceBroadcastReceiver"
    private lateinit var auth: FirebaseAuth
    private val geofenceD = Firebase.database.reference.child("Geofence")

    override fun onReceive(context: Context, intent: Intent) {
        val notificationHelper = NotificationHelper(context)
        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        auth = FirebaseAuth.getInstance()
        val userId = FirebaseAuth.getInstance().uid

        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Error receiving geofence even...")
            return
        }
        var geofenceList: List<Geofence> = geofencingEvent.triggeringGeofences

        val transitionType = geofencingEvent.geofenceTransition
        for (geofence in geofenceList) {
            Log.d(TAG, "onReceive" + geofence.requestId)
        }

        // More to be added
        when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                Log.d(TAG, "Transition Enter")
                Toast.makeText(context, "Geofence Transition Enter", Toast.LENGTH_SHORT).show()
                // Sets database value to true if user is within device range
                if (userId != null) {
                    geofenceD.child(userId).setValue(true)
                }
                //userId?.let { geofenceD.child(it).child("Location") }
                    //?.setValue(true)
                // If time permits add body saying what mode its at
                notificationHelper.sendHighPriorityNotification(
                    "Blinds and Windows Set",
                    "",
                    MainActivity::class.java
                )
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                Log.d(TAG, "Transition Exit")
                Toast.makeText(context, "Geofence Transition Exit", Toast.LENGTH_SHORT).show()
                // Sets database value to false if user is within device range
                if (userId != null) {
                    geofenceD.child(userId).setValue(false)
                }
                //userId?.let { geofenceD.child(it).child("Location") }
                    //?.setValue(false)
                notificationHelper.sendHighPriorityNotification(
                    "Blinds and Windows Closed",
                    "",
                    MainActivity::class.java
                )
            }
        }

    }
}