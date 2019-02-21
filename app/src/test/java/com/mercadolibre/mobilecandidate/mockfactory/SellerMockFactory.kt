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

        fun otherPowerSellerStatus(): Seller {
            return Seller(
                "id",
                "nickname",
                Seller.Address("city", "state"),
                Seller.Reputation("")
            )
        }

        fun nullPowerSellerStatus(): Seller {
            return Seller(
                "id",
                "nickname",
                Seller.Address("city", "state"),
                Seller.Reputation(null)
            )
        }

        fun nullSeller(): Seller {
            return Seller(
                "id",
                null,
                null,
                null
            )
        }

        fun nullCity(): Seller {
            return Seller(
                "id",
                null,
                Seller.Address(null, "state"),
                null
            )
        }

        fun nullState(): Seller {
            return Seller(
                "id",
                null,
                Seller.Address("city", null),
                null
            )
        }
    }
}