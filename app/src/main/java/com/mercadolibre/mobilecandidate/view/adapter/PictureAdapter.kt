package com.mercadolibre.mobilecandidate.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.koushikdutta.ion.Ion
import com.mercadolibre.mobilecandidate.R
import com.mercadolibre.mobilecandidate.model.Product
import kotlinx.android.synthetic.main.item_picture.view.*

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 20/02/2019
 * ************************************************************
 */
class PictureAdapter(private val pictures: ArrayList<Product.Picture>) : RecyclerView.Adapter<PictureAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return PictureAdapter.ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_picture, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return pictures.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val picture = pictures[position]
        Ion.with(holder.photo).load(picture.secure_url)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photo = itemView.iv_photo_picture!!
    }
}