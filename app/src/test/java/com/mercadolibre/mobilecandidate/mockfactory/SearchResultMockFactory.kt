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
                SearchResult.Paging(200, 0, 50, 200)
            )
        }

        fun secondPage(): SearchResult {
            return SearchResult(
                "chromecast",
                ProductMockFactory.simpleList(),
                SearchResult.Paging(200, 50, 50, 200)
            )
        }

        fun thirdPage(): SearchResult {
            return SearchResult(
                "chromecast",
                ProductMockFactory.simpleList(),
                SearchResult.Paging(200, 100, 50, 200)
            )
        }

        fun lastPage(): SearchResult {
            return SearchResult(
                "chromecast",
                ProductMockFactory.simpleList(),
                SearchResult.Paging(200, 200, 50, 200)
            )
        }
    }
}