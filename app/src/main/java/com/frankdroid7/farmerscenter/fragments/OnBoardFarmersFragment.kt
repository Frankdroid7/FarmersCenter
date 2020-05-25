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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.frankdroid7.farmerscenter.R
import com.frankdroid7.farmerscenter.adapter.convertToString
import com.frankdroid7.farmerscenter.shouldSaveDataToDb
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.phelat.navigationresult.BundleFragment
import kotlinx.android.synthetic.main.fragment_on_board_farmers.*
import kotlinx.android.synthetic.main.fragment_on_board_farmers.view.*


class OnBoardFarmersFragment : BundleFragment(), GoogleApiClient.OnConnectionFailedListener {

    private lateinit var replyBundle: Bundle
    val REQUEST_IMAGE_CAPTURE = 1
    private var farmersImageBitmap: Bitmap? = null
    private var coordinatesPicked: Boolean = false
    private var isCenterCrop: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    findNavController().navigateUp()
                    shouldSaveDataToDb = false
                }


            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        replyBundle = bundleOf()

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

            if (isCenterCrop) {
                farmers_onboard_img.scaleType = ImageView.ScaleType.CENTER_CROP
            }
            farmersImageBitmap?.let {
                farmers_onboard_img.setImageBitmap(it)
            }
            onboard_farmers_button.setOnClickListener {

                if (farmers_onboard_name.text.toString().isEmpty()) {
                    farmers_onboard_name.error = "Must not be empty"
                    return@setOnClickListener
                }
                if (farmers_onboard_age.text.toString().isEmpty()) {
                    farmers_onboard_age.error = "Must not be empty"
                    return@setOnClickListener
                }
                if (farmersImageBitmap == null) {
                    showToast(context, "Take Farmers Photograph")
                    return@setOnClickListener
                }
                if (farm_onboard_name.text.toString().isEmpty()) {
                    farm_onboard_name.error = "Must not be empty"
                    return@setOnClickListener
                }
                if (farm_onboard_location.text.toString().isEmpty()) {
                    farm_onboard_location.error = "Must not be empty"
                    return@setOnClickListener
                }
                if (!coordinatesPicked) {
                    showToast(context, "Pick Coordinates")
                    return@setOnClickListener
                }

                arguments?.let {
                    replyBundle.putString(FARMERS_NAME, farmers_onboard_name.text.toString())
                    replyBundle.putString(FARMERS_AGE, farmers_onboard_age.text.toString())
                    replyBundle.putString(FARMERS_IMG, farmersImageBitmap?.convertToString())
                    replyBundle.putString(FARM_NAME, farm_onboard_name.text.toString())
                    replyBundle.putString(FARM_LOCATION, farm_onboard_location.text.toString())
                }

                shouldSaveDataToDb = true
                findNavController().navigate(R.id.action_onBoardFarmersFragment_to_homeScreenFragment, replyBundle)
            }

            farmers_onboard_img.setOnClickListener { captureFarmersPhotograph() }

            farm_onboard_coordinates.setOnClickListener {
                if (isNetworkActive()) {
                    if (isLocationEnabled(requireContext())) {
                        val action =
                            OnBoardFarmersFragmentDirections.actionOnBoardFarmersFragmentToMapsFragment()
                        navigate(action, 100)
                    } else {
                        showToast(requireContext(), "Please SWITCH ON your location")
                    }
                } else {
                    showToast(requireContext(), "CHECK YOUR NETWORK CONNECTION")
                }
            }
        }
    }

    override fun onFragmentResult(requestCode: Int, bundle: Bundle) {
        super.onFragmentResult(requestCode, bundle)


        replyBundle.putDouble(FARM_LAT1, bundle.getDouble("lat1"))
        replyBundle.putDouble(FARM_LON1, bundle.getDouble("lon1"))
        replyBundle.putDouble(FARM_LAT2, bundle.getDouble("lat2"))
        replyBundle.putDouble(FARM_LON2, bundle.getDouble("lon2"))
        replyBundle.putDouble(FARM_LAT3, bundle.getDouble("lat3"))
        replyBundle.putDouble(FARM_LON3, bundle.getDouble("lon3"))
        replyBundle.putDouble(FARM_LAT4, bundle.getDouble("lat4"))
        replyBundle.putDouble(FARM_LON4, bundle.getDouble("lon4"))

        coordinatesPicked = true

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
            farmers_onboard_img.scaleType = ImageView.ScaleType.CENTER_CROP
            isCenterCrop = true

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
