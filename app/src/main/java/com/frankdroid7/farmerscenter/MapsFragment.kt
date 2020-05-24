package com.frankdroid7.farmerscenter

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.navigateUp
import com.frankdroid7.farmerscenter.fragments.showToast
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.phelat.navigationresult.navigateUp
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.android.synthetic.main.fragment_maps.save_coordinate_btn
import kotlinx.android.synthetic.main.fragment_maps.set_polygon_btn
import kotlinx.android.synthetic.main.fragment_maps.view.*


class MapsFragment : Fragment(), OnMapReadyCallback {

    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var map: GoogleMap
    private var latlng1: LatLng? = null
    private var latlng2: LatLng? = null
    private var latlng3: LatLng? = null
    private var latlng4: LatLng? = null
    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null
    var count = 0
    private var bundle = Bundle()
    private lateinit var setPolygonBtn: Button
    private lateinit var continueBtn: Button


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        map_view.onCreate(savedInstanceState)
        map_view.onResume()
        map_view.getMapAsync(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.apply {
            save_coordinate_btn.setOnClickListener {
                if (count == 4) {
                    Log.e("BUNDLE", bundle.toString())
                    navigateUp(100, bundle)
                } else {
                    showToast(context, "Pick 4 points before you continue.")
                }
            }
            setPolygonBtn = set_polygon_btn
            continueBtn = save_coordinate_btn

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        googleMap.setOnMapClickListener { latLng ->
            count++
            when (count) {
                1 -> {
                    latlng1 = LatLng(latLng.latitude, latLng.longitude)
                    map.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                latLng.latitude,
                                latLng.longitude
                            )
                        )
                    )
                }
                2 -> {
                    latlng2 = LatLng(latLng.latitude, latLng.longitude)
                    map.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                latLng.latitude,
                                latLng.longitude
                            )
                        )
                    )
                }
                3 -> {
                    latlng3 = LatLng(latLng.latitude, latLng.longitude)
                    map.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                latLng.latitude,
                                latLng.longitude
                            )
                        )
                    )
                }
                4 -> {
                    latlng4 = LatLng(latLng.latitude, latLng.longitude)
                    map.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                latLng.latitude,
                                latLng.longitude
                            )
                        )
                    )
                    bundle.apply {
                        putDouble("lat1", latlng1!!.latitude)
                        putDouble("lon1", latlng1!!.longitude)

                        putDouble("lat2", latlng2!!.latitude)
                        putDouble("lon2", latlng2!!.longitude)

                        putDouble("lat3", latlng3!!.latitude)
                        putDouble("lon3", latlng3!!.longitude)

                        putDouble("lat4", latlng4!!.latitude)
                        putDouble("lon4", latlng4!!.longitude)
                    }
                }
                else -> Toast.makeText(
                    context,
                    "You can only pick 4 coordinates",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }

        arguments?.let {

            val polygon1: Polygon = googleMap.addPolygon(
                PolygonOptions()
                    .clickable(true)
                    .add(
                        LatLng(it.getDouble("mLat1"), it.getDouble("mLon1")),
                        LatLng(it.getDouble("mLat2"), it.getDouble("mLon2")),
                        LatLng(it.getDouble("mLat3"), it.getDouble("mLon3")),
                        LatLng(it.getDouble("mLat4"), it.getDouble("mLon4"))
                    )
            )

            if (it.getBoolean("fromDetailsScreen")) {
                setPolygonBtn.visibility = View.GONE
                continueBtn.visibility = View.GONE
            }else{
                setPolygonBtn.visibility = View.VISIBLE
                continueBtn.visibility = View.VISIBLE
            }
            polygon1.tag = "alpha"
            polygon1.fillColor = Color.RED
        }

        setPolygonBtn.setOnClickListener {
            if (count < 4) {
                Toast.makeText(context, "Pick 4 points on the map", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val polygon1: Polygon = googleMap.addPolygon(
                PolygonOptions()
                    .clickable(true)
                    .add(
                        latlng1,
                        latlng2,
                        latlng3,
                        latlng4
                    )
            )
            polygon1.tag = "alpha"
            polygon1.fillColor = Color.RED

        }

        locationRequest = LocationRequest.create()
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest!!.interval = 20 * 1000
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {

                        currentLongitude = location.longitude
                        currentLatitude = location.latitude
                        val homeLatLng = LatLng(currentLatitude!!, currentLongitude!!)
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, 18f))
                    }
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }
}
