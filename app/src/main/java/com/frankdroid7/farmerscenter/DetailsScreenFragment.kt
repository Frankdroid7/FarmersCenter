package com.frankdroid7.farmerscenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.frankdroid7.farmerscenter.adapter.convertToBitMap
import kotlinx.android.synthetic.main.fragment_details_screen.*
import com.frankdroid7.farmerscenter.fragments.OnBoardFarmersFragment.Companion.FARMERS_AGE
import com.frankdroid7.farmerscenter.fragments.OnBoardFarmersFragment.Companion.FARMERS_IMG
import com.frankdroid7.farmerscenter.fragments.OnBoardFarmersFragment.Companion.FARMERS_NAME
import com.frankdroid7.farmerscenter.fragments.OnBoardFarmersFragment.Companion.FARM_LOCATION
import com.frankdroid7.farmerscenter.fragments.OnBoardFarmersFragment.Companion.FARM_NAME

class DetailsScreenFragment : Fragment() {

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
        }
    }

}
