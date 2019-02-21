package com.mercadolibre.mobilecandidate.mockfactory

import com.mercadolibre.mobilecandidate.model.Product

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 20/02/2019
 * ************************************************************
 */
class ShippingMockFactory {
    companion object {
        fun trueFreeShipping(): Product.Shipping? {
            return Product.Shipping(true)
        }

        fun falseFreeShipping(): Product.Shipping? {
            return Product.Shipping(false)
        }

        fun nullShipping(): Product.Shipping? {
            return null
        }

        fun nullFreeShipping(): Product.Shipping? {
            return Product.Shipping(null)
        }
    }
}