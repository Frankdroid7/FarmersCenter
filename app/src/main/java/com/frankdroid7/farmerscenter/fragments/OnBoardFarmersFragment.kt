package com.frankdroid7.farmerscenter.fragments

//import com.google.android.gms.location.places.GeoDataClient
//import com.google.android.gms.location.places.Places
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Toast
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

        return LayoutInflater.from(context).inflate(R.layout.fragment_on_board_farmers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {

            onboard_farmers_button.setOnClickListener {
                val replyBundle = bundleOf()
                if (TextUtils.isEmpty(farmers_onboard_name.text.toString())) {
                    Toast.makeText(context, "All fields must be filled", Toast.LENGTH_LONG).show()
                } else {

                    replyBundle.putString(FARMERS_NAME, farmers_onboard_name.text.toString())
                    replyBundle.putString(FARMERS_AGE, farmers_onboard_age.text.toString())
                    replyBundle.putString(FARMERS_IMG, farmersImageBitmap.convertToString())
                    replyBundle.putString(FARM_NAME, farm_onboard_name.text.toString())
                    replyBundle.putString(FARM_LOCATION, farm_onboard_location.text.toString())

                    findNavController().navigate(R.id.homeScreenFragment, replyBundle)

                }
            }

            farmers_onboard_img.setOnClickListener {
                captureFarmersPhotograph()
            }

            farm_onboard_coordinates.setOnClickListener {
                findNavController().navigate(R.id.mapsFragment)
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

companion object{
    const val FARMERS_NAME = "com.frankdroid7.farmerscenter.FARMERS_NAME"
    const val FARMERS_AGE = "com.frankdroid7.farmerscenter.FARMERS_AGE"
    const val FARMERS_IMG = "com.frankdroid7.farmerscenter.FARMERS_IMG"
    const val FARM_NAME = "com.frankdroid7.farmerscenter.FARM_NAME"
    const val FARM_LOCATION = "com.frankdroid7.farmerscenter.FARM_LOCATION"
    const val FARM_COORDINATES = "com.frankdroid7.farmerscenter.FARM_COORDINATES"
}

    override fun onConnectionFailed(p0: ConnectionResult) {

    }
}
