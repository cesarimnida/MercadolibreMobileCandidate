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
import timber.log.Timber

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 19/02/2019
 * ************************************************************
 */
class SearchViewModel(application: Application) :
    AndroidViewModel(application) {
    constructor(
        application: Application,
        searchService: SearchService
    ) : this(application) {
        this.searchService = searchService
    }

    private var searchService: SearchService = SearchService()
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
                Timber.e(e)
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

    fun navigateToPreviousPage() {
        if (!isSearchResultStatusSuccess() || !isPreviousOffsetValid()) return
        val currentOffset = searchResult.value!!.data!!.paging.offset
        val limit = searchResult.value!!.data!!.paging.limit
        fetchProducts(
            searchResult.value!!.data!!.query,
            currentOffset - limit
        )
    }

    private fun isSearchResultStatusSuccess(): Boolean {
        return searchResult.value!!.status == StatusEvent.Status.SUCCESS
    }

    private fun isPreviousOffsetValid(): Boolean {
        val offset = searchResult.value?.data?.paging?.offset ?: -1
        val limit = searchResult.value?.data?.paging?.limit ?: 0
        return offset >= limit
    }

    fun navigateToNextPage() {
        if (!isSearchResultStatusSuccess() || !isNextOffsetValid()) return
        val currentOffset = searchResult.value!!.data!!.paging.offset
        val limit = searchResult.value!!.data!!.paging.limit
        fetchProducts(
            searchResult.value!!.data!!.query,
            currentOffset + limit
        )
    }

    private fun isNextOffsetValid(): Boolean {
        val offset = searchResult.value?.data?.paging?.offset ?: 0
        val limit = searchResult.value?.data?.paging?.limit ?: 0
        val total = searchResult.value?.data?.paging?.total ?: Int.MAX_VALUE
        return offset + limit <= total
    }

    fun currentPage(paging: SearchResult.Paging): String {
        val currentPage = calculateCurrentPage(paging) + 1
        return currentPage.toString()
    }

    private fun calculateCurrentPage(paging: SearchResult.Paging): Int {
        return paging.offset / paging.limit
    }

    fun previousPage(paging: SearchResult.Paging): String {
        val previousPage = calculateCurrentPage(paging)
        return previousPage.toString()
    }

    fun nextPage(paging: SearchResult.Paging): String {
        val nextPage = calculateCurrentPage(paging) + 2
        return nextPage.toString()
    }

    fun previousPageVisibility(): Int {
        return if (isPreviousOffsetValid()) View.VISIBLE else View.INVISIBLE
    }

    fun nextPageVisibility(): Int {
        return if (isNextOffsetValid()) View.VISIBLE else View.INVISIBLE
    }
}