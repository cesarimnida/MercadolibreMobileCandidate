package com.mercadolibre.mobilecandidate.mockfactory

import com.mercadolibre.mobilecandidate.model.SearchResult

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 21/02/2019
 * ************************************************************
 */
class SearchResultMockFactory {
    companion object {
        fun filled(): SearchResult {
            return SearchResult(
                "chromecast",
                ProductMockFactory.simpleList(),
                SearchResult.Paging(10, 0, 50, 10)
            )
        }
    }
}