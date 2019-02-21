package com.mercadolibre.mobilecandidate.model

import java.io.Serializable

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 19/02/2019
 * ************************************************************
 */
class Product(
    var id: String,
    var title: String?,
    var seller: Seller,
    var price: String?,
    var currency_id: String?,
    var available_quantity: String?,
    var condition: String?,
    var thumbnail: String?,
    var installments: Installment?,
    var shipping: Shipping?,
    var attributes: ArrayList<Attribute>?,
    var reviews: Review?,
    var pictures: ArrayList<Picture>?
) : Serializable {
    inner class Review(
        var rating_average: Double?,
        var total: Int?
    ) : Serializable

    inner class Picture(
        var id: String?,
        var secure_url: String?
    ) : Serializable {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Picture

            if (id != other.id) return false
            if (secure_url != other.secure_url) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id?.hashCode() ?: 0
            result = 31 * result + (secure_url?.hashCode() ?: 0)
            return result
        }
    }

    inner class Attribute(
        var id: String?,
        var name: String?,
        var value_name: String?
    ) : Serializable {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Attribute

            if (id != other.id) return false
            if (name != other.name) return false
            if (value_name != other.value_name) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id?.hashCode() ?: 0
            result = 31 * result + (name?.hashCode() ?: 0)
            result = 31 * result + (value_name?.hashCode() ?: 0)
            return result
        }
    }

    class Installment(
        var quantity: Int?,
        var amount: Double?,
        var rate: Double?,
        var currency_id: String?
    ) : Serializable

    class Shipping(
        var free_shipping: Boolean?
    ) : Serializable

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (id != other.id) return false
        if (title != other.title) return false
        if (seller != other.seller) return false
        if (price != other.price) return false
        if (currency_id != other.currency_id) return false
        if (available_quantity != other.available_quantity) return false
        if (condition != other.condition) return false
        if (thumbnail != other.thumbnail) return false
        if (installments != other.installments) return false
        if (shipping != other.shipping) return false
        if (attributes != other.attributes) return false
        if (reviews != other.reviews) return false
        if (pictures != other.pictures) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + seller.hashCode()
        result = 31 * result + (price?.hashCode() ?: 0)
        result = 31 * result + (currency_id?.hashCode() ?: 0)
        result = 31 * result + (available_quantity?.hashCode() ?: 0)
        result = 31 * result + (condition?.hashCode() ?: 0)
        result = 31 * result + (thumbnail?.hashCode() ?: 0)
        result = 31 * result + (installments?.hashCode() ?: 0)
        result = 31 * result + (shipping?.hashCode() ?: 0)
        result = 31 * result + (attributes?.hashCode() ?: 0)
        result = 31 * result + (reviews?.hashCode() ?: 0)
        result = 31 * result + (pictures?.hashCode() ?: 0)
        return result
    }

}