package com.frankdroid7.farmerscenter.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
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
import com.frankdroid7.farmerscenter.fragments.OnBoardFarmersFragment.Companion.FARM_LOCATION
import com.frankdroid7.farmerscenter.fragments.OnBoardFarmersFragment.Companion.FARM_NAME
import kotlinx.android.synthetic.main.fragment_home_screen.view.*


class HomeScreenFragment : Fragment() {

    private val homeScreenFragmentRequestCode = 1
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


        arguments?.let {
            val farmersData = FarmersData(
                0,
                arguments?.getString(FARMERS_NAME)!!,
                arguments?.getString(FARMERS_AGE)!!,
                arguments?.getString(FARMERS_IMG)!!,
                arguments?.getString(FARM_NAME)!!,
                arguments?.getString(FARM_LOCATION)!!

            )
            farmersViewModel.insert(farmersData)
        }

         farmersAdapter = FarmersAdapter(requireContext())
        farmersViewModel.allFarmersData.observe(viewLifecycleOwner, Observer{farmersData ->

            farmersData?.let {
                farmersAdapter.setFarmersData(it)
            }
        })

        view.home_screen_toolbar.inflateMenu(R.menu.home_menu)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.apply {
            farmersAdapter.onClickListener = {farmersData ->

                val bundle = bundleOf()
                bundle.apply {
                    putString(FARMERS_NAME, farmersData.farmers_name)
                    putString(FARMERS_AGE, farmersData.farmers_age)
                    putString(FARMERS_IMG, farmersData.farmers_image)
                    putString(FARM_NAME, farmersData.farm_name)
                    putString(FARM_LOCATION, farmersData.farm_location)
                }
                findNavController().navigate(R.id.detailsScreenFragment, bundle)

            }
            farmersAdapter.popUpListener = {id, view ->

                val popup = PopupMenu(context, view)
                popup.inflate(R.menu.popup_menu)
                popup.setOnMenuItemClickListener { menuItem ->
                    when(menuItem.itemId){
                        R.id.delete_record_menu -> farmersViewModel.deleteFarmersDataById(id)
                    }
                    return@setOnMenuItemClickListener true
                }
                popup.show()
            }
            main_recyclerView.adapter = farmersAdapter
            main_recyclerView.layoutManager = GridLayoutManager(context, 2)
            home_fab.setOnClickListener {

                findNavController().navigate(
                    HomeScreenFragmentDirections
                        .actionHomeScreenFragmentToOnBoardFarmersFragment())
            }
        }
    }

}
fun showToast(ctx: Context?, msg: String){
    Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show()
}