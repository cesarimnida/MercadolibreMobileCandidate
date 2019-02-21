package com.mercadolibre.mobilecandidate.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.koushikdutta.ion.Ion
import com.mercadolibre.mobilecandidate.R
import com.mercadolibre.mobilecandidate.model.Product
import com.mercadolibre.mobilecandidate.view.activity.ProductDetailActivity
import com.mercadolibre.mobilecandidate.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.item_product.view.*

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 19/02/2019
 * ************************************************************
 */
class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private val products = ArrayList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ProductAdapter.ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_product, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.title.text = product.title
        holder.totalValue.setValue(product.price)
        holder.totalValue.setUnit(product.currency_id)
        Ion.with(holder.photo).load(product.thumbnail)
        holder.freeShipping.visibility = SearchViewModel.freeShippingVisibility(product.shipping)
        val installment = product.installments
        if (installment == null) {
            holder.installmentValue.setValue(0.0)
            holder.installmentValue.setUnit("")
            holder.installmentValue.visibility = View.GONE
            holder.interestFree.visibility = View.GONE
        } else {
            holder.installmentValue.setValue(installment.amount)
            holder.installmentValue.setUnit(SearchViewModel.buildInstallmentUnit(installment))
            holder.interestFree.visibility = SearchViewModel.interestFreeVisibility(installment)
            holder.installmentValue.visibility = View.VISIBLE
        }
        holder.item.setOnClickListener { ProductDetailActivity.startActivity(holder.item.context, product) }
    }

    fun updateList(products: ArrayList<Product>?) {
        this.products.clear()
        this.products.addAll(products ?: ArrayList())
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item = itemView.cl_product!!
        val title = itemView.tv_title_product!!
        val photo = itemView.iv_photo_product!!
        val totalValue = itemView.tv_total_value_product!!
        val installmentValue = itemView.tv_installment_value_product!!
        val interestFree = itemView.tv_interest_free_product!!
        val freeShipping = itemView.tv_free_shipping_product!!
    }
}