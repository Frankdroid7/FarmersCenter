package com.frankdroid7.farmerscenter.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.frankdroid7.farmerscenter.R
import com.frankdroid7.farmerscenter.database.FarmersData

class FarmersAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<FarmersAdapter.FarmersViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var farmersData = emptyList<FarmersData>() // Cached copy of words

    inner class FarmersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val farmersImg = itemView.findViewById<ImageView>(R.id.item_farmers_img)
        val farmName = itemView.findViewById<TextView>(R.id.item_farm_name_textV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FarmersViewHolder {
        val itemView = inflater.inflate(R.layout.main_recycler_view_layout, parent, false)
        return FarmersViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FarmersViewHolder, position: Int) {
        val currentFarmersData = farmersData[position]
        holder.apply {
            farmName.text = currentFarmersData.farm_name
            farmersImg.setImageBitmap(currentFarmersData.farmers_photo.convertToBitMap())
        }
    }

    internal fun setFarmersData(farmersData: List<FarmersData>) {
        this.farmersData = farmersData
        notifyDataSetChanged()
    }

    override fun getItemCount() = farmersData.size
}

private fun String.convertToBitMap(): Bitmap? {
    return try {
        val encodeByte = Base64.decode(this, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    } catch (e: Exception) {
        null
    }

}
