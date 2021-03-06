package com.frankdroid7.farmerscenter.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.frankdroid7.farmerscenter.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.apply {

            login_btn.setOnClickListener {

                if (login_email.text.toString().isEmpty()){
                    login_email.error = "Must not be empty"
                    return@setOnClickListener
                }
                if (login_pswd.text.toString().isEmpty()){
                    login_pswd.error = "Must not be empty"
                    return@setOnClickListener

                }
                if (login_email.text.toString() == "test@theagromall.com"
                    && login_pswd.text.toString() == "password"){

                    login_btn.visibility = View.GONE
                    login_pb.visibility = View.VISIBLE

                    Handler().postDelayed({
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeScreenFragment())
                    }, 3000)
                }else{
                    Snackbar.make(login_llt_root,
                        "Wrong Credentials, please try again.", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

}
