package com.example.smartwindowsapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.smartwindowsapp.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_maps.*


const val LOCATION_ACCESS_REQUEST_CODE = 10001
const val CAMERA_ZOOM_LEVEL = 17f
const val RADIUS = 40.0
const val TAG = "MapsActivity" // Debugging

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    // Realtime Database to store Device location
    private val mapsD = Firebase.database.reference.child("DeviceLocation")
    private val latD = mapsD.child("deviceLatitude")
    private val longD = mapsD.child("deviceLongitude")

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var cDLocation: LatLng // Current Location

    private var circle: Circle?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // This enables back button in actionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // FusedLocationProvider to obtain current location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.setAllGesturesEnabled(false)
        map.uiSettings.isMapToolbarEnabled = false

        // Receive device latitude and longitude values from Firebase
        // Default Set to Alumni Hall
        var currentDLat = 43.006000487147425
        var currentDLong = -81.27477160512986

        // Dark maps
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.style_json
                )
            )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }

        latD.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot) {
                val currentDLatD = datasnapshot.getValue<Double>() // temp value
                Log.d(TAG, "Received Lat Data")
                if (currentDLatD != null) {
                    currentDLat = currentDLatD
                }
                // Device Home Location
                cDLocation = LatLng(currentDLat, currentDLong)
                with(map){
                    clear()
                    addMarker(cDLocation)
                    addCircle(cDLocation)
                    moveCamera(CameraUpdateFactory.newLatLngZoom(cDLocation, CAMERA_ZOOM_LEVEL))
                    // Whichever database listener acts second overrides previous action with both
                    // long and lat values
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        longD.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(datasnapshot: DataSnapshot) {
                val currentDLongD = datasnapshot.getValue<Double>() // temp value
                Log.d(TAG, "Received Long Data")
                if (currentDLongD != null) {
                    currentDLong = currentDLongD
                }
                // Device Home Location
                cDLocation = LatLng(currentDLat, currentDLong)
                with(map) {
                    clear()
                    addMarker(cDLocation)
                    addCircle(cDLocation)
                    moveCamera(CameraUpdateFactory.newLatLngZoom(cDLocation, CAMERA_ZOOM_LEVEL))
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        // When button is clicked new device location is set as current location
        btn_currentLocation.setOnClickListener{
            enableUserLocation()
        }
    }

    private fun enableUserLocation(){
        //Fix Location Permission
        // If user has already granted location permission, enable location
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED){
            map.isMyLocationEnabled = true
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                cDLocation = LatLng(it.latitude, it.longitude)
                latD.setValue(it.latitude)
                longD.setValue(it.longitude)
                Log.d(TAG, "Updated Device Location Data")
                Toast.makeText(this@MapsActivity, "Device Home Location Set", Toast.LENGTH_LONG).show() // Move when geofence is created
                with(map) {
                    // Removes previous set device location markers
                    clear()
                    addMarker(cDLocation)
                    addCircle(cDLocation)
                    moveCamera(CameraUpdateFactory.newLatLngZoom(cDLocation, CAMERA_ZOOM_LEVEL))
                }
            }
        }
        else{
            // Ask for Permission
            if(ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
                    return
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) !=PackageManager.PERMISSION_GRANTED )
                {
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), 102)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == LOCATION_ACCESS_REQUEST_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // We have the permission
                map.isMyLocationEnabled = true
            }else{
                // We do not have the permission
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun addMarker(latLng: LatLng){
        map.addMarker(MarkerOptions()
            .position(latLng).title("Device Home Location")
        )
    }

    private fun addCircle(latLng: LatLng){
        circle = map.addCircle(
            CircleOptions()
                .center(latLng)
                .radius(RADIUS)
                .strokeColor(Color.argb(255, 255, 0, 0))
                .fillColor(Color.argb(64, 255, 0, 0))
        )
    }
}