package com.mercadolibre.mobilecandidate.service

import com.mercadolibre.mobilecandidate.model.Product
import com.mercadolibre.mobilecandidate.model.ProductDescription
import com.mercadolibre.mobilecandidate.model.SearchResult
import com.mercadolibre.mobilecandidate.model.Seller
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 19/02/2019
 * ************************************************************
 */
interface SearchInterface {
    @GET("/sites/MLU/search")
    fun queryProducts(@Query("q") query: String?, @Query("offset") offset: Int): Single<SearchResult>

    @GET("/items/{itemId}")
    fun fetchDetailedProduct(@Path("itemId") itemId: String): Single<Product>

    @GET("/items/{itemId}/description")
    fun fetchProductDescription(@Path("itemId") itemId: String): Single<ProductDescription>

    @GET("/users/{userId}")
    fun fetchSeller(@Path("userId") userId: String): Single<Seller>
}