package com.frankdroid7.farmerscenter.fragments

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.frankdroid7.farmerscenter.R
import kotlinx.android.synthetic.main.fragment_on_board_farmers.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class OnBoardFarmersFragment : Fragment() {

    val REQUEST_IMAGE_CAPTURE = 1

    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_board_farmers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.apply {

            imageView = farmers_photo_img
            farmers_photo_img.setOnClickListener {
                captureFarmersPhotograph()
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
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)

        }

    }

    private fun Bitmap.convertToString(): String{
        val baos =  ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG,100, baos)
        val byteArray= baos.toByteArray()
        val bitMapString = Base64.encodeToString(byteArray, Base64.DEFAULT)
        return  bitMapString

    }

}
