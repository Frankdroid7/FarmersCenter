package com.frankdroid7.farmerscenter

import android.os.Bundle
import com.phelat.navigationresult.FragmentResultActivity

class MainActivity : FragmentResultActivity() {
    override fun getNavHostFragmentId() =  R.id.fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
