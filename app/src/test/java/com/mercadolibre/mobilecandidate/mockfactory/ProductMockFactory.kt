package com.mercadolibre.mobilecandidate.mockfactory

import com.mercadolibre.mobilecandidate.model.Product
import com.mercadolibre.mobilecandidate.model.Seller

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 20/02/2019
 * ************************************************************
 */
class ProductMockFactory {
    companion object {
        fun filled(): Product {
            return Product(
                "id",
                "title",
                SellerMockFactory.filled(),
                "price",
                "currency_id",
                "available_quantity",
                "condition",
                "thumbnail",
                null,
                null,
                null,
                null,
                null
            )
        }

        fun simpleList(): ArrayList<Product> {
            val products = ArrayList<Product>()
            for (i in 0..10) products.add(filled())
            return products
        }
    }
}