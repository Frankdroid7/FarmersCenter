package com.frankdroid7.farmerscenter.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.frankdroid7.farmerscenter.R
import kotlinx.android.synthetic.main.fragment_home_screen.view.*


class HomeScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        // Inflate the layout for this fragment
        val view =
            inflater.inflate(R.layout.fragment_home_screen, container, false)

        view.home_screen_toolbar.inflateMenu(R.menu.home_menu)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {
            home_fab.setOnClickListener {
                findNavController().navigate(R.id.onBoardFarmersFragment)
            }
        }
    }

}
