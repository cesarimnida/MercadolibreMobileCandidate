package com.mercadolibre.mobilecandidate.model

import java.io.Serializable

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 19/02/2019
 * ************************************************************
 */
class SearchResult(
    val query: String,
    val results: ArrayList<Product>,
    val paging: Paging
) : Serializable {
    class Paging(
        var total: Int,
        var offset: Int,
        var limit: Int,
        var primary_results: Int
    ) : Serializable
}