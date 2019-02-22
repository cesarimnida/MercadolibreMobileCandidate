package com.mercadolibre.mobilecandidate.view.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mercadolibre.mobilecandidate.R
import com.mercadolibre.mobilecandidate.model.Product
import com.mercadolibre.mobilecandidate.model.ProductDescription
import com.mercadolibre.mobilecandidate.model.Seller
import com.mercadolibre.mobilecandidate.view.adapter.AttributesAdapter
import com.mercadolibre.mobilecandidate.view.adapter.PictureAdapter
import com.mercadolibre.mobilecandidate.viewmodel.ProductViewModel
import com.mercadolibre.mobilecandidate.viewmodel.SearchViewModel
import com.mercadolibre.mobilecandidate.viewmodel.StatusEvent
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.content_product_detail.*
import kotlinx.android.synthetic.main.content_seller_info.*

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 19/02/2019
 * ************************************************************
 */
class ProductDetailActivity : AppCompatActivity() {
    companion object {
        private const val PRODUCT =
            "com.mercadolibre.mobilecandidate.view.activity.ProductDetailActivity.PRODUCT"

        fun startActivity(context: Context, product: Product) {
            val i = Intent(context, ProductDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(PRODUCT, product)
            i.putExtras(bundle)
            context.startActivity(i)
        }
    }

    private lateinit var viewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        initViewModel()
        setProduct()
        observeProduct()
        fetchMoreDetails()
        observeDetailedProduct()
        observeProductDescription()
        observeSeller()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
    }

    private fun setProduct() {
        val product = intent?.extras?.getSerializable(PRODUCT) as Product
        viewModel.setProduct(product)
    }

    private fun observeProduct() {
        viewModel.product.observe(this, Observer { product -> handleProduct(product) })
    }

    private fun handleProduct(product: Product?) {
        if (product == null) return
        setProductDetail(product)
        setAttributes(product.attributes)
    }

    private fun setProductDetail(product: Product) {
        tv_title_product_detail.text = product.title
        pt_price_product_detail.setValue(product.price)
        pt_price_product_detail.setUnit(product.currency_id)
        setReview(product.reviews)
        tv_condition_product_detail.text = getString(viewModel.productCondition(product.condition))
        setInstallment(product.installments)
        tv_free_shipping_product_detail.visibility = SearchViewModel.freeShippingVisibility(product.shipping)
        tv_available_quantity_product_detail.text =
            String.format(getString(R.string.tv_available_quantity_product_detail), product.available_quantity)
    }

    private fun setReview(review: Product.Review?) {
        if (review == null) {
            cr_rating_product_detail.visibility = View.GONE
            tv_rating_product_detail.visibility = View.GONE
            return
        }
        cr_rating_product_detail.rating = review.rating_average?.toFloat() ?: 0f
        tv_rating_product_detail.text =
            String.format(getString(R.string.tv_rating_product_detail), review.total?.toString() ?: "0")
    }

    private fun setInstallment(installment: Product.Installment?) {
        if (installment == null) {
            pt_installment_price_product_detail.visibility = View.GONE
            return
        }
        pt_installment_price_product_detail.setValue(installment.amount?.toString() ?: "0")
        pt_installment_price_product_detail.setUnit(SearchViewModel.buildInstallmentUnit(installment))
    }

    private fun setAttributes(attributes: ArrayList<Product.Attribute>?) {
        if (attributes == null) {
            return
        }
        rv_attributes_product_detail.layoutManager = LinearLayoutManager(this)
        rv_attributes_product_detail.adapter = AttributesAdapter(attributes)
    }

    private fun fetchMoreDetails() {
        viewModel.fetchDetailedProduct()
        viewModel.fetchProductDescription()
        viewModel.fetchSeller()
    }

    private fun observeDetailedProduct() {
        viewModel.detailedProduct.observe(this, Observer { statusEvent -> handleDetailedProduct(statusEvent) })
    }

    private fun handleDetailedProduct(detailedProduct: StatusEvent<Product>?) {
        if (detailedProduct == null) return
        when (detailedProduct.status) {
            StatusEvent.Status.SUCCESS -> {
                setPhotosRecyclerView(detailedProduct.data!!.pictures)
                stopPictureLoading()
            }
            StatusEvent.Status.LOADING -> {
                startPictureLoading()
            }
            StatusEvent.Status.ERROR -> {
                stopPictureLoading()
                showPictureError()
            }
        }
    }

    private fun startPictureLoading() {
        pb_pictures_product_detail.visibility = View.VISIBLE
        rv_pictures_product_detail.visibility = View.GONE
    }

    private fun stopPictureLoading() {
        pb_pictures_product_detail.visibility = View.GONE
        rv_pictures_product_detail.visibility = View.VISIBLE
    }

    private fun showPictureError() {
        cv_empty_photos_product_detail.visibility = View.VISIBLE
    }

    private fun setPhotosRecyclerView(pictures: ArrayList<Product.Picture>?) {
        if (pictures == null || pictures.isEmpty()) return
        rv_pictures_product_detail.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_pictures_product_detail.adapter = PictureAdapter(pictures)
    }

    private fun observeProductDescription() {
        viewModel.productDescription.observe(
            this,
            Observer { productDescription -> handleProductDescription(productDescription) })
    }

    private fun handleProductDescription(productDescription: StatusEvent<ProductDescription>?) {
        if (productDescription == null) {
            tv_description_label_product_detail.visibility = View.GONE
            tv_description_product_detail.visibility = View.GONE
            return
        }
        when (productDescription.status) {
            StatusEvent.Status.SUCCESS -> {
                tv_description_product_detail.text = productDescription.data!!.plain_text
                stopProductDescriptionLoading()
            }
            StatusEvent.Status.LOADING -> {
                startProductDescriptionLoading()
            }
            StatusEvent.Status.ERROR -> {
                stopProductDescriptionLoading()
                showProductDescriptionError()
            }
        }
    }

    private fun startProductDescriptionLoading() {
        pb_description_product_detail.visibility = View.VISIBLE
        tv_description_product_detail.visibility = View.GONE
    }

    private fun stopProductDescriptionLoading() {
        pb_description_product_detail.visibility = View.GONE
        tv_description_product_detail.visibility = View.VISIBLE
    }

    private fun showProductDescriptionError() {
        tv_description_product_detail.text = getString(R.string.product_description_error)
    }

    private fun observeSeller() {
        viewModel.seller.observe(this, Observer { seller -> handleSeller(seller) })
    }

    private fun handleSeller(seller: StatusEvent<Seller>?) {
        if (seller == null) return
        when (seller.status) {
            StatusEvent.Status.SUCCESS -> {
                tv_seller_name_product_detail.text = seller.data!!.nickname
                tv_seller_platinum_product_detail.visibility =
                    viewModel.platinumVisibility(seller.data.seller_reputation)
                iv_seller_platinum_product_detail.visibility =
                    viewModel.platinumVisibility(seller.data.seller_reputation)
                tv_seller_location_product_detail.text = viewModel.sellerLocation(seller.data.address)
                stopSellerLoading()
            }
            StatusEvent.Status.LOADING -> {
                startSellerLoading()
            }
            StatusEvent.Status.ERROR -> {
                stopSellerLoading()
                showSellerError()
            }
        }
    }

    private fun startSellerLoading() {
        pb_seller_info_product_detail.visibility = View.VISIBLE
        cl_seller_info_product_detail.visibility = View.GONE
    }

    private fun stopSellerLoading() {
        pb_seller_info_product_detail.visibility = View.GONE
        cl_seller_info_product_detail.visibility = View.VISIBLE
    }

    private fun showSellerError() {
        cl_seller_info_product_detail.visibility = View.GONE
        cv_empty_seller_info_product_detail.visibility = View.VISIBLE
    }
}
