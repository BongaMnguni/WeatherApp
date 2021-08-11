package com.bongamnguni.weather.ui

import android.location.Address
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bongamnguni.weather.R
import com.bongamnguni.weather.adaptors.FavoriteAdaptor
import com.bongamnguni.weather.database.FavoriteModel
import com.bongamnguni.weather.databinding.ActivityMapsBinding
import com.bongamnguni.weather.repository.ForecastViewModel

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.maps.*
import java.io.IOException


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var favoriteAdaptor: FavoriteAdaptor
    private lateinit var forecastViewModel: ForecastViewModel

    private var cordList : MutableList<LatLng> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
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

        mMap = googleMap

        favoriteAdaptor = FavoriteAdaptor(mutableListOf())

        forecastViewModel = ViewModelProvider(this).get(ForecastViewModel::class.java)
        forecastViewModel.getFavorite().observe(this, Observer<List<FavoriteModel>> { favorite ->

            repeat(favorite.size) {
                if (Geocoder.isPresent()) {
                    try {
                        var gc = Geocoder(this)
                        var addresses: List<Address> = gc.getFromLocationName(favorite[it].place, 5) // get the found Address Objects
                        cordList = ArrayList(addresses.size) // A list to save the coordinates if they are available
                        for (a in addresses) {
                            if (a.hasLatitude() && a.hasLongitude()) {

                                mMap.addMarker(MarkerOptions().position(LatLng(a.latitude,a.longitude)).title(favorite[it].place))

                                //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                            }
                        }
                    } catch (e: IOException) {
                        // handle the exception
                        Log.i("Bonga", e.stackTrace.toString())
                    }
                }
            }
        })
    }
}