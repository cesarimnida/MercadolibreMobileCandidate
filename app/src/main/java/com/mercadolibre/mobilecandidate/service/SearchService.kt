package com.mercadolibre.mobilecandidate.service

import com.google.gson.GsonBuilder
import com.mercadolibre.mobilecandidate.model.Product
import com.mercadolibre.mobilecandidate.model.ProductDescription
import com.mercadolibre.mobilecandidate.model.SearchResult
import com.mercadolibre.mobilecandidate.model.Seller
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 19/02/2019
 * ************************************************************
 */
class SearchService : SearchInterface {
    private val searchInterface = searchInterface()
    override fun queryProducts(query: String?, offset: Int): Single<SearchResult> {
        return searchInterface
            .queryProducts(query, offset)
    }

    override fun fetchDetailedProduct(itemId: String): Single<Product> {
        return searchInterface
            .fetchDetailedProduct(itemId)
    }

    override fun fetchProductDescription(itemId: String): Single<ProductDescription> {
        return searchInterface
            .fetchProductDescription(itemId)
    }

    override fun fetchSeller(userId: String): Single<Seller> {
        return searchInterface
            .fetchSeller(userId)
    }

    private fun searchInterface(): SearchInterface {
        val builder = Retrofit.Builder()
            .baseUrl("https://api.mercadolibre.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        val retrofit = builder.client(okHttpClient()).build()
        return retrofit.create(SearchInterface::class.java)
    }

    private fun okHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        logging.level = HttpLoggingInterceptor.Level.BODY
        okHttpClientBuilder.addNetworkInterceptor(logging)
        okHttpClientBuilder.addInterceptor(interceptor)
        okHttpClientBuilder.readTimeout(2, TimeUnit.MINUTES)
        okHttpClientBuilder.connectTimeout(2, TimeUnit.MINUTES)
        okHttpClientBuilder.writeTimeout(2, TimeUnit.MINUTES)
        return okHttpClientBuilder.build()
    }
}