package com.frankdroid7.farmerscenter.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.frankdroid7.farmerscenter.FarmersViewModel
import com.frankdroid7.farmerscenter.R
import com.frankdroid7.farmerscenter.adapter.FarmersAdapter
import com.frankdroid7.farmerscenter.database.FarmersData
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
import com.frankdroid7.farmerscenter.shouldSaveDataToDb
import kotlinx.android.synthetic.main.fragment_home_screen.*
import kotlinx.android.synthetic.main.fragment_home_screen.view.*


class HomeScreenFragment : Fragment() {

    private lateinit var listOfFarmersData: List<FarmersData>
    private lateinit var farmersAdapter: FarmersAdapter
    private lateinit var farmersViewModel: FarmersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        // To Inflate the layout for this fragment
        val view =
            inflater.inflate(R.layout.fragment_home_screen, container, false)

        farmersViewModel = ViewModelProvider(this).get(FarmersViewModel::class.java)


        if (shouldSaveDataToDb) {
            val farmersData = FarmersData(
                0,
                arguments?.getString(FARMERS_NAME)!!,
                arguments?.getString(FARMERS_AGE)!!,
                arguments?.getString(FARMERS_IMG)!!,
                arguments?.getString(FARM_NAME)!!,
                arguments?.getString(FARM_LOCATION)!!,
                arguments?.getDouble(FARM_LAT1)!!,
                arguments?.getDouble(FARM_LON1)!!,
                arguments?.getDouble(FARM_LAT2)!!,
                arguments?.getDouble(FARM_LON2)!!,
                arguments?.getDouble(FARM_LAT3)!!,
                arguments?.getDouble(FARM_LON3)!!,
                arguments?.getDouble(FARM_LAT4)!!,
                arguments?.getDouble(FARM_LON4)!!
            )

            farmersViewModel.insert(farmersData)
        }

        farmersAdapter = FarmersAdapter(requireContext())
        farmersViewModel.allFarmersData.observe(viewLifecycleOwner, Observer { farmersData ->

            if (farmersData.isEmpty()) {
                main_recyclerView.visibility = View.GONE
                no_records_linearL.visibility = View.VISIBLE
            }
            farmersData?.let {

                listOfFarmersData = it
                farmersAdapter.setFarmersData(it)
                main_recyclerView.adapter = farmersAdapter
                main_recyclerView.layoutManager = GridLayoutManager(context, 2)
            }
        })

        view.home_screen_toolbar.inflateMenu(R.menu.home_menu)
        view.home_screen_toolbar.setOnMenuItemClickListener { item: MenuItem? ->
            when (item?.itemId) {
                R.id.about_us_menu -> showToast(context, "About Us")
                R.id.contact_us_menu -> showToast(context, "Contact Us")
                R.id.delete_all_records_menu -> {
                    farmersViewModel.deleteAllRecords()
                    showToast(context, "All records Deleted")
                }
            }
            return@setOnMenuItemClickListener true
        }
        return view
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.apply {
            farmersAdapter.onClickListener = { farmersData ->

                val bundle = bundleOf()
                bundle.apply {
                    putString(FARMERS_NAME, farmersData.farmers_name)
                    putString(FARMERS_AGE, farmersData.farmers_age)
                    putString(FARMERS_IMG, farmersData.farmers_image)
                    putString(FARM_NAME, farmersData.farm_name)
                    putString(FARM_LOCATION, farmersData.farm_location)
                    putDouble(FARM_LAT1, farmersData.farm_lat1)
                    putDouble(FARM_LON1, farmersData.farm_lon1)
                    putDouble(FARM_LAT2, farmersData.farm_lat2)
                    putDouble(FARM_LON2, farmersData.farm_lon2)
                    putDouble(FARM_LAT3, farmersData.farm_lat3)
                    putDouble(FARM_LON3, farmersData.farm_lon3)
                    putDouble(FARM_LAT4, farmersData.farm_lat4)
                    putDouble(FARM_LON4, farmersData.farm_lon4)


                }

                findNavController().navigate(
                    R.id.action_homeScreenFragment_to_detailsScreenFragment,
                    bundle
                )

            }
            farmersAdapter.popUpListener = { id, view ->

                val popup = PopupMenu(context, view)
                popup.inflate(R.menu.popup_menu)

                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.delete_record_menu -> {
                            farmersViewModel.deleteFarmersDataById(id)
                            showToast(context, "Deleted")
                        }
                    }
                    return@setOnMenuItemClickListener true
                }
                popup.show()
            }

            home_fab.setOnClickListener {

                findNavController().navigate(
                    HomeScreenFragmentDirections
                        .actionHomeScreenFragmentToOnBoardFarmersFragment()
                )
            }

            search_farm_editT.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(newText: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val userInput = newText!!.toString().toLowerCase()
                    val newList: ArrayList<FarmersData> = arrayListOf()

                    for (eachFarmersData in listOfFarmersData) {
                        if (eachFarmersData.farm_name.toLowerCase().contains(userInput)) {
                            newList.add(eachFarmersData)
                        }
                    }
                    farmersAdapter.updateList(newList)
                }

            })
        }
    }


}

fun showToast(ctx: Context?, msg: String) {
    Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
}