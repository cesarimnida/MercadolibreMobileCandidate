package com.mercadolibre.mobilecandidate.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.view.View
import com.mercadolibre.mobilecandidate.model.Product
import com.mercadolibre.mobilecandidate.model.SearchResult
import com.mercadolibre.mobilecandidate.service.SearchService
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 19/02/2019
 * ************************************************************
 */
class SearchViewModel(application: Application, private val searchService: SearchService = SearchService()) :
    AndroidViewModel(application) {
    val searchResult = MutableLiveData<StatusEvent<SearchResult>>()
    val products = MutableLiveData<ArrayList<Product>>()


    companion object {
        fun buildInstallmentUnit(installment: Product.Installment): String {
            if (installment.quantity == null || installment.currency_id == null) return ""
            return installment.quantity.toString() + "x " + installment.currency_id
        }

        fun freeShippingVisibility(shipping: Product.Shipping?): Int {
            return if (shipping?.free_shipping == true) View.VISIBLE else View.GONE
        }

        fun interestFreeVisibility(installment: Product.Installment): Int {
            return if (installment.rate == 0.0) View.VISIBLE else View.GONE
        }
    }

    fun fetchProducts(query: String?, offset: Int = 0) {
        val observer = searchResultObserver()
        searchService
            .queryProducts(query, offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { t -> observer.onError(t) }
            .subscribe(observer)
    }


    private fun searchResultObserver(): SingleObserver<SearchResult> {
        return object : SingleObserver<SearchResult> {
            override fun onSubscribe(d: Disposable) {
                searchResult.value = StatusEvent.loading()
            }

            override fun onSuccess(searchResult: SearchResult) {
                this@SearchViewModel.searchResult.value = StatusEvent.success(searchResult)
                products.value = searchResult.results
            }

            override fun onError(e: Throwable) {
                searchResult.value = StatusEvent.error(e)
            }
        }
    }

    fun setSearchResult(searchResult: SearchResult) {
        this.searchResult.value = StatusEvent.success(searchResult)
        products.value = searchResult.results
    }

    fun removeSearchResult() {
        this.searchResult.value = null
    }
}