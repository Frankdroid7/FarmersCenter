package com.frankdroid7.farmerscenter

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.findFragment
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.android.synthetic.main.fragment_maps.*
import java.util.*


class MapsFragment : Fragment(), OnMapReadyCallback, LocationListener {

    private lateinit var map: GoogleMap
    private var latlng1: LatLng? = null
    private var latlng2: LatLng? = null
    private var latlng3: LatLng? = null
    private var latlng4: LatLng? = null
    var count = 0


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        map_view.onCreate(savedInstanceState)
        map_view.onResume()

        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 10)

            return
        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0F, this)

        map_view.getMapAsync(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10){
            Log.e("PERMISSION", "GRANTED")

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mView = inflater.inflate(R.layout.fragment_maps, container, false)

        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), getString(R.string.api_key))
        }



        // Inflate the layout for this fragment
        return mView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap


        googleMap.setOnMapClickListener { latLng ->

            map.addMarker(MarkerOptions().position(LatLng(latLng.latitude, latLng.longitude)))
            count++


            when (count) {
                1 -> latlng1 = LatLng(latLng.latitude, latLng.longitude)
                2 -> latlng2 = LatLng(latLng.latitude, latLng.longitude)
                3 -> latlng3 = LatLng(latLng.latitude, latLng.longitude)
                4 -> latlng4 = LatLng(latLng.latitude, latLng.longitude)
                else -> Toast.makeText(context, "You can only click 3 times", Toast.LENGTH_LONG)
                    .show()
            }
        }

        set_polygon_btn.setOnClickListener {

            if (count < 4) {
                Toast.makeText(context, "Pick 4 points", Toast.LENGTH_LONG).show()
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

        val homeLatLng = LatLng(6.6803518, 3.3547928)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, 18f))


    }

    override fun onLocationChanged(location: Location?) {

    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }


}
