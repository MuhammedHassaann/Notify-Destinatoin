package com.muhammedhassaan.notifydestiniation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.muhammedhassaan.notifydestiniation.databinding.ActivityMapsBinding
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback ,EasyPermissions.PermissionCallbacks , LocationListener{

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        //request background location permission
        getBackgroundLocation()

        //get current location
        currentLocation()

        map.setOnMapLongClickListener { latLng ->
            addMarker(latLng)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

    @SuppressLint("MissingPermission")
    private fun currentLocation() {
        if (hasForegroundLocationPermission()){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                MIN_TIME_UPDATE,
                MIN_DISTANCE_UPDATE,
                this)
        }else{
            requestForegroundLocationPermission()
        }
    }

    override fun onLocationChanged(location: Location) {
        val latLng = LatLng(location.latitude,location.longitude)
        setPosition(latLng)
    }

    private fun addMarker(latLng: LatLng) {

        with(map){
            addMarker(MarkerOptions().position(latLng).title(getAddress(latLng))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
        }
    }

    private fun getAddress(latLng: LatLng): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val province =
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)?.get(0)?.adminArea
        val city =
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)?.get(0)?.locality
        return "$province - $city"
    }

    private fun moveCamera(latLng: LatLng){
        with(map){
            moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoomLevel))
        }
    }

    private fun setPosition(latLng: LatLng){
        addMarker(latLng)
        moveCamera(latLng)
    }

    private fun hasBackgroundLocationPermission() =
        EasyPermissions.hasPermissions(this,BACKGROUND_LOCATION_PERMISSION)
    private fun hasForegroundLocationPermission() =
        EasyPermissions.hasPermissions(this,FINE_LOACTION_PERMISSION,COARSE_LOCATION_PERMISSION)


    private fun requestBackgroundLocationPermission(){
        EasyPermissions.requestPermissions(this,
            "This application cannot work without background location permission",
            BACKGROUND_LOCATION_REQUEST,
            BACKGROUND_LOCATION_PERMISSION
        )
    }
    private fun requestForegroundLocationPermission(){
        EasyPermissions.requestPermissions(this,
            "This application cannot work without location permission",
            FOREGROUND_LOCATION_REQUEST,
            FINE_LOACTION_PERMISSION,
            COARSE_LOCATION_PERMISSION
        )
    }

    private fun getBackgroundLocation(){
        if(!hasBackgroundLocationPermission()){
            requestBackgroundLocationPermission()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        when(requestCode){
            FOREGROUND_LOCATION_REQUEST -> {
                if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
                    SettingsDialog.Builder(this).build().show()
                }else{
                    requestForegroundLocationPermission()
                }
            }
            BACKGROUND_LOCATION_REQUEST -> {
                if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
                    SettingsDialog.Builder(this).build().show()
                }else{
                    requestBackgroundLocationPermission()
                }
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Snackbar.make(this,binding.root,"Location Permission Granted",Snackbar.ANIMATION_MODE_FADE).show()
    }



    //map settings
    private val zoomLevel = 15f

    //checking if the api is lower or higher than api 29
    private val RUNNING_Q_OR_LOWER = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q


    //foreground permission location
    private val FOREGROUND_LOCATION_REQUEST = 1
    private val FINE_LOACTION_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val COARSE_LOCATION_PERMISSION = android.Manifest.permission.ACCESS_COARSE_LOCATION

    //background permission
    private val BACKGROUND_LOCATION_REQUEST = 2
    private val BACKGROUND_LOCATION_PERMISSION = android.Manifest.permission.ACCESS_BACKGROUND_LOCATION

    private val MIN_TIME_UPDATE = 60 * 1000L
    private val MIN_DISTANCE_UPDATE = 0f

}