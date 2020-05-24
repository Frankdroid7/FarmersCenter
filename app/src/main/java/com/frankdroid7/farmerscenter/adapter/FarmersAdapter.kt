package com.frankdroid7.farmerscenter.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.frankdroid7.farmerscenter.R
import com.frankdroid7.farmerscenter.database.FarmersData
import java.io.ByteArrayOutputStream

class FarmersAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<FarmersAdapter.FarmersViewHolder>() {

    private lateinit var filteredFarmersList: ArrayList<FarmersData>

    var onClickListener = {farmersData: FarmersData ->}
    var popUpListener = {id: Int, view: View -> }
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var farmersData = emptyList<FarmersData>() // Cached copy of words

    inner class FarmersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val farmersImg = itemView.findViewById<ImageView>(R.id.item_farmers_img)!!
        val farmName = itemView.findViewById<TextView>(R.id.item_farm_name_textV)!!
        val farmCardRtV = itemView.findViewById<CardView>(R.id.item_card_rtV)!!
        val popUpBtn = itemView.findViewById<ImageButton>(R.id.item_popup_btn)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FarmersViewHolder {
        val itemView = inflater.inflate(R.layout.main_recycler_view_layout, parent, false)
        return FarmersViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FarmersViewHolder, position: Int) {
        val currentFarmersData = farmersData[position]
        holder.apply {
            popUpBtn.setOnClickListener { popUpListener(currentFarmersData.id, popUpBtn) }
            farmCardRtV.setOnClickListener { onClickListener(currentFarmersData) }
            farmName.text = currentFarmersData.farm_name
            farmersImg.setImageBitmap(currentFarmersData.farmers_image.convertToBitMap())
        }
    }

    internal fun setFarmersData(farmersData: List<FarmersData>) {

        this.farmersData = farmersData.distinct()
        farmersData.forEach { eachFarmersData ->
            Log.e("F_farmersData", eachFarmersData.farm_name)
        }
        notifyDataSetChanged()
    }

    fun updateList(newListOfFarmersData: ArrayList<FarmersData>) {
        filteredFarmersList = arrayListOf()
        filteredFarmersList.addAll(newListOfFarmersData)
        farmersData = filteredFarmersList
        notifyDataSetChanged()
    }

    override fun getItemCount() = farmersData.size
}


fun String.convertToBitMap(): Bitmap? {
    return try {
        val encodeByte = Base64.decode(this, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    } catch (e: Exception) {
        null
    }

}

fun Bitmap.convertToString(): String {
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, baos)
    val byteArray = baos.toByteArray()
    val bitMapString = Base64.encodeToString(byteArray, Base64.DEFAULT)
    return bitMapString

}
