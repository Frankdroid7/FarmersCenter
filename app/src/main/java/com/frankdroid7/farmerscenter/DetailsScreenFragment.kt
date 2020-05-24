package com.frankdroid7.farmerscenter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.frankdroid7.farmerscenter.adapter.convertToBitMap
import kotlinx.android.synthetic.main.fragment_details_screen.*
import com.frankdroid7.farmerscenter.fragments.OnBoardFarmersFragment.Companion.FARMERS_AGE
import com.frankdroid7.farmerscenter.fragments.OnBoardFarmersFragment.Companion.FARMERS_IMG
import com.frankdroid7.farmerscenter.fragments.OnBoardFarmersFragment.Companion.FARMERS_NAME
import com.frankdroid7.farmerscenter.fragments.OnBoardFarmersFragment.Companion.FARM_LAT1
import com.frankdroid7.farmerscenter.fragments.OnBoardFarmersFragment.Companion.FARM_LAT2
import com.frankdroid7.farmerscenter.fragments.OnBoardFarmersFragment.Companion.FARM_LAT3
import com.frankdroid7.farmerscenter.fragments.OnBoardFarmersFragment.Companion.FARM_LAT4
import com.frankdroid7.farmerscenter.fragments.OnBoardFarmersFragment.Companion.FARM_LOCATION
import com.frankdroid7.farmerscenter.fragments.OnBoardFarmersFragment.Companion.FARM_LON1
import com.frankdroid7.farmerscenter.fragments.OnBoardFarmersFragment.Companion.FARM_LON2
import com.frankdroid7.farmerscenter.fragments.OnBoardFarmersFragment.Companion.FARM_LON3
import com.frankdroid7.farmerscenter.fragments.OnBoardFarmersFragment.Companion.FARM_LON4
import com.frankdroid7.farmerscenter.fragments.OnBoardFarmersFragment.Companion.FARM_NAME
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_details_screen.show_farm_coordinate_btn
import kotlinx.android.synthetic.main.fragment_details_screen.view.*

class DetailsScreenFragment : Fragment() {

    private var latlng1: LatLng? = null
    private var latlng2: LatLng? = null
    private var latlng3: LatLng? = null
    private var latlng4: LatLng? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        requireActivity().onBackPressedDispatcher
//            .addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    //Handle back event from any fragment
//                    findNavController().navigateUp()
//                }
//            })
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.let{
            details_farmers_name.text = it.getString(FARMERS_NAME)
            details_farmers_age.text = it.getString(FARMERS_AGE)
            details_farmers_img.setImageBitmap(it.getString(FARMERS_IMG)?.convertToBitMap())
            details_toolbar.title = it.getString(FARM_NAME)
            details_farm_location.text = it.getString(FARM_LOCATION)
            latlng1 = LatLng(it.getDouble(FARM_LAT1), it.getDouble(FARM_LON1))
            latlng2 = LatLng(it.getDouble(FARM_LAT2), it.getDouble(FARM_LON2))
            latlng3 = LatLng(it.getDouble(FARM_LAT3), it.getDouble(FARM_LON3))
            latlng4 = LatLng(it.getDouble(FARM_LAT4), it.getDouble(FARM_LON4))


        }

        view.apply {
            val mapBundle = Bundle()
            mapBundle.apply {
                putDouble("mLat1", latlng1!!.latitude)
                putDouble("mLon1", latlng1!!.longitude)
                putDouble("mLat2", latlng2!!.latitude)
                putDouble("mLon2", latlng2!!.longitude)
                putDouble("mLat3", latlng3!!.latitude)
                putDouble("mLon3", latlng3!!.longitude)
                putDouble("mLat4", latlng4!!.latitude)
                putDouble("mLon4", latlng4!!.longitude)


            }
            show_farm_coordinate_btn.setOnClickListener {
                findNavController().navigate(R.id.mapsFragment, mapBundle)
            }
        }
    }

}
