package com.example.storyappsumbission.ui

import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.storyappsumbission.R
import com.example.storyappsumbission.data.UiState
import com.example.storyappsumbission.databinding.ActivityMapsBinding
import com.example.storyappsumbission.response.ListStoryItem
import com.example.storyappsumbission.viewmodel.MapsViewModel
import com.example.storyappsumbission.viewmodel.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val viewModel: MapsViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "MAPS STORY"

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.apply {
            mMap.uiSettings.isZoomControlsEnabled = true
            mMap.uiSettings.isIndoorLevelPickerEnabled = true
            mMap.uiSettings.isCompassEnabled = true
            mMap.uiSettings.isMapToolbarEnabled = true
        }
        addManyMarker()
        setMapStyle()
        getMyLocation()

        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
    private fun setMapStyle(){
        try{
            val success=
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success){
                Log.e(TAG, "Style parsing failed")
            }
        }catch (exception: Resources.NotFoundException){
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }
    companion object{
        private const val TAG = "MapActivity"
    }
    private fun addManyMarker(){
        viewModel.getLocationStory().observe(this){
            when (it){
                is UiState.Loading -> {

                }
                is UiState.Success ->{
                    addMarker(it.data)
                }
                is UiState.Error ->{

                }
            }
        }
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ){ isGranted: Boolean ->
            if (isGranted){
                getMyLocation()
            }
        }
    private fun addMarker(location: List<ListStoryItem>){
        val boundsBuilder = LatLngBounds.Builder()
        location.forEach{data ->
            val userLocation = LatLng(data.lat!!, data.lon!!)
            mMap.addMarker(
                MarkerOptions()
                    .position(userLocation)
                    .title(data.name)
                    .snippet(data.description)
            )
            boundsBuilder.include(userLocation)
        }
        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                200/2
            )
        )
    }
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

}
