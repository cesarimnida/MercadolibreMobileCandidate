package com.mercadolibre.mobilecandidate.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mercadolibre.mobilecandidate.R
import com.mercadolibre.mobilecandidate.model.Product
import kotlinx.android.synthetic.main.item_attribute.view.*

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 20/02/2019
 * ************************************************************
 */
class AttributesAdapter(private val attributes: ArrayList<Product.Attribute>) :
    RecyclerView.Adapter<AttributesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return AttributesAdapter.ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_attribute, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return attributes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val attribute = attributes[position]
        holder.name.text = attribute.name
        holder.value.text = attribute.value_name
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.tv_name_attribute!!
        val value = itemView.tv_value_attribute!!
    }
}