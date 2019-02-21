package com.mercadolibre.mobilecandidate.mockfactory

import com.mercadolibre.mobilecandidate.model.Seller

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 21/02/2019
 * ************************************************************
 */
class SellerMockFactory {
    companion object {
        fun filled(): Seller {
            return Seller(
                "id",
                "nickname",
                Seller.Address("city", "state"),
                Seller.Reputation("platinum")
            )
        }
    }
}