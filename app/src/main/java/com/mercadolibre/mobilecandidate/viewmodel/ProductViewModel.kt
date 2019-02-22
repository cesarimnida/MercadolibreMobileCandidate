package com.mercadolibre.mobilecandidate.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.mercadolibre.mobilecandidate.R
import com.mercadolibre.mobilecandidate.model.Product
import com.mercadolibre.mobilecandidate.model.ProductDescription
import com.mercadolibre.mobilecandidate.model.Seller
import com.mercadolibre.mobilecandidate.service.SearchService
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 19/02/2019
 * ************************************************************
 */
class ProductViewModel(application: Application) :
    AndroidViewModel(application) {
    constructor(
        application: Application,
        searchService: SearchService
    ) : this(application) {
        this.searchService = searchService
    }

    private var searchService: SearchService = SearchService()
    val product = MutableLiveData<Product>()
    val detailedProduct = MutableLiveData<StatusEvent<Product>>()
    val productDescription = MutableLiveData<StatusEvent<ProductDescription>>()
    val seller = MutableLiveData<StatusEvent<Seller>>()

    fun setProduct(product: Product) {
        this.product.value = product
    }

    fun fetchDetailedProduct() {
        val observer = detailedProductObserver()
        searchService
            .fetchDetailedProduct(product.value!!.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { t -> observer.onError(t) }
            .subscribe(observer)
    }

    private fun detailedProductObserver(): SingleObserver<Product> {
        return object : SingleObserver<Product> {
            override fun onSubscribe(d: Disposable) {
                detailedProduct.value = StatusEvent.loading()
            }

            override fun onSuccess(detailedProduct: Product) {
                this@ProductViewModel.detailedProduct.value = StatusEvent.success(detailedProduct)
            }

            override fun onError(e: Throwable) {
                detailedProduct.value = StatusEvent.error(e)
                Timber.e(e)
            }
        }
    }

    fun fetchProductDescription() {
        val observer = productDescriptionObserver()
        searchService
            .fetchProductDescription(product.value!!.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { t -> observer.onError(t) }
            .subscribe(observer)
    }

    private fun productDescriptionObserver(): SingleObserver<ProductDescription> {
        return object : SingleObserver<ProductDescription> {
            override fun onSubscribe(d: Disposable) {
                productDescription.value = StatusEvent.loading()
            }

            override fun onSuccess(productDescription: ProductDescription) {
                this@ProductViewModel.productDescription.value = StatusEvent.success(productDescription)
            }

            override fun onError(e: Throwable) {
                productDescription.value = StatusEvent.error(e)
                Timber.e(e)
            }
        }
    }

    fun fetchSeller() {
        val observer = sellerObserver()
        searchService
            .fetchSeller(product.value!!.seller.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { t -> observer.onError(t) }
            .subscribe(observer)
    }

    private fun sellerObserver(): SingleObserver<Seller> {
        return object : SingleObserver<Seller> {
            override fun onSubscribe(d: Disposable) {
                seller.value = StatusEvent.loading()
            }

            override fun onSuccess(seller: Seller) {
                this@ProductViewModel.seller.value = StatusEvent.success(seller)
            }

            override fun onError(e: Throwable) {
                seller.value = StatusEvent.error(e)
                Timber.e(e)
            }
        }
    }

    fun platinumVisibility(seller_reputation: Seller.Reputation?): Int {
        if (seller_reputation == null) return View.GONE
        return if (seller_reputation.power_seller_status == "platinum") View.VISIBLE else View.GONE
    }

    fun sellerLocation(address: Seller.Address?): String? {
        if (address == null) return ""
        val city = address.city ?: "-"
        val state = address.state ?: "-"
        return "$city/$state"
    }

    fun productCondition(condition: String?): Int {
        return when (condition) {
            null -> R.string.empty_string
            "new" -> R.string.new_product
            else -> R.string.used_product
        }
    }
}