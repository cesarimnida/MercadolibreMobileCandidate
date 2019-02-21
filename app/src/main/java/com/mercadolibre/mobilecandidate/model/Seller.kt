package com.mercadolibre.mobilecandidate.model

import java.io.Serializable

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 19/02/2019
 * ************************************************************
 */
class Seller(
    var id: String,
    var nickname: String?,
    var address: Address?,
    var seller_reputation: Reputation?
) : Serializable {
    class Reputation(
        var power_seller_status: String?
    ) : Serializable

    inner class Transaction(
        var canceled: String?,
        var completed: String?,
        var period: String?,
        var ratings: Rating?,
        var total: String?
    ) : Serializable

    inner class Rating(
        var negative: Double?,
        var neutral: Double?,
        var positive: Double?
    ) : Serializable

    class Address(
        var city: String?,
        var state: String?
    ) : Serializable

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Seller

        if (id != other.id) return false
        if (nickname != other.nickname) return false
        if (address?.city != other.address?.city) return false
        if (address?.state != other.address?.state) return false
        if (seller_reputation?.power_seller_status != other.seller_reputation?.power_seller_status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (nickname?.hashCode() ?: 0)
        result = 31 * result + (address?.hashCode() ?: 0)
        result = 31 * result + (seller_reputation?.hashCode() ?: 0)
        return result
    }


}