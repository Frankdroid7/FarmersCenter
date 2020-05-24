package com.frankdroid7.farmerscenter.fragments


import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.contentValuesOf
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.frankdroid7.farmerscenter.R
//import com.frankdroid7.farmerscenter.adapter.PlaceAutocompleteAdapter
import com.frankdroid7.farmerscenter.adapter.convertToString
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.fragment_on_board_farmers.*
import kotlinx.android.synthetic.main.fragment_on_board_farmers.view.*


class OnBoardFarmersFragment : Fragment(), GoogleApiClient.OnConnectionFailedListener {

    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var farmersImageBitmap: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 10)

        }

        return LayoutInflater.from(context)
            .inflate(R.layout.fragment_on_board_farmers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {

            onboard_farmers_button.setOnClickListener {
                val replyBundle = bundleOf()
                if (TextUtils.isEmpty(farmers_onboard_name.text.toString())) {
                    Toast.makeText(context, "All fields must be filled", Toast.LENGTH_LONG).show()
                } else {

                    arguments?.let {

                        replyBundle.putString(FARMERS_NAME, farmers_onboard_name.text.toString())
                        replyBundle.putString(FARMERS_AGE, farmers_onboard_age.text.toString())
                        replyBundle.putString(FARMERS_IMG, farmersImageBitmap.convertToString())
                        replyBundle.putString(FARM_NAME, farm_onboard_name.text.toString())
                        replyBundle.putString(FARM_LOCATION, farm_onboard_location.text.toString())
                        replyBundle.putDouble(FARM_LAT1, it.getDouble("lat1"))
                        replyBundle.putDouble(FARM_LON1, it.getDouble("lon1"))
                        replyBundle.putDouble(FARM_LAT2, it.getDouble("lat2"))
                        replyBundle.putDouble(FARM_LON2, it.getDouble("lon2"))
                        replyBundle.putDouble(FARM_LAT3, it.getDouble("lat3"))
                        replyBundle.putDouble(FARM_LON3, it.getDouble("lon3"))
                        replyBundle.putDouble(FARM_LAT4, it.getDouble("lat4"))
                        replyBundle.putDouble(FARM_LON4, it.getDouble("lon4"))
                    }

                    findNavController().navigate(R.id.homeScreenFragment, replyBundle)

                }
            }

            farmers_onboard_img.setOnClickListener {
                captureFarmersPhotograph()

            }

            farm_onboard_coordinates.setOnClickListener {
//
                if (isNetworkActive()) {
                    if (isLocationEnabled(requireContext())){
                        val action = OnBoardFarmersFragmentDirections.actionOnBoardFarmersFragmentToMapsFragment()
                        findNavController().navigate(action)
                    }else{
                        showToast(requireContext(), "Please SWITCH ON your location")
                    }
                } else {
                    showToast(requireContext(), "CHECK YOUR NETWORK CONNECTION")
                }
            }
        }
    }

    private fun captureFarmersPhotograph() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            farmersImageBitmap = data!!.extras!!.get("data") as Bitmap
            farmers_onboard_img.setImageBitmap(farmersImageBitmap)

        }

    }

    companion object {
        const val FARMERS_NAME = "com.frankdroid7.farmerscenter.FARMERS_NAME"
        const val FARMERS_AGE = "com.frankdroid7.farmerscenter.FARMERS_AGE"
        const val FARMERS_IMG = "com.frankdroid7.farmerscenter.FARMERS_IMG"
        const val FARM_NAME = "com.frankdroid7.farmerscenter.FARM_NAME"
        const val FARM_LOCATION = "com.frankdroid7.farmerscenter.FARM_LOCATION"
        const val FARM_LAT1 = "com.frankdroid7.farmerscenter.LAT1"
        const val FARM_LON1 = "com.frankdroid7.farmerscenter.L0N1"
        const val FARM_LAT2 = "com.frankdroid7.farmerscenter.LAT2"
        const val FARM_LON2 = "com.frankdroid7.farmerscenter.LON2"
        const val FARM_LAT3 = "com.frankdroid7.farmerscenter.LAT3"
        const val FARM_LON3 = "com.frankdroid7.farmerscenter.LON3"
        const val FARM_LAT4 = "com.frankdroid7.farmerscenter.LAT4"
        const val FARM_LON4 = "com.frankdroid7.farmerscenter.LON4"
    }

    override fun onConnectionFailed(p0: ConnectionResult) {}

    fun isNetworkActive(): Boolean {
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo

        val isInternetConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected

        return isInternetConnected
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                if (isLocationEnabled(requireContext())) {

                } else {
                    showToast(requireContext(), "Please SWITCH ON your location")

                }
            } else {
                showToast(requireContext(), "PERMISSION NEEDED")
            }

        }

    }

    fun isLocationEnabled(context: Context): Boolean {
        var enabled = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is new method provided in API 28
            val lm =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            enabled = lm.isLocationEnabled
        }
        return enabled
    }

}


fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}
