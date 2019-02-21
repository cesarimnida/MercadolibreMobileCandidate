package com.mercadolibre.mobilecandidate.mockfactory

import com.mercadolibre.mobilecandidate.model.Product
import com.nhaarman.mockito_kotlin.mock

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 20/02/2019
 * ************************************************************
 */
class InstallmentMockFactory {
    companion object {
        fun filled(): Product.Installment {
            return Product.Installment(10, 50.5, 0.0, "R$")
        }

        fun allNull(): Product.Installment {
            return Product.Installment(null, null, null, null)
        }

        fun nullQuantity(): Product.Installment {
            return Product.Installment(null, 50.5, 0.0, "R$")
        }

        fun nullUnit(): Product.Installment {
            return Product.Installment(10, 50.5, 0.0, null)
        }

        fun withRate(): Product.Installment {
            return Product.Installment(10, 50.5, 5.0, null)
        }

        fun nullRate(): Product.Installment {
            return Product.Installment(10, 50.5, null, null)
        }
    }
}