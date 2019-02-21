package com.mercadolibre.mobilecandidate.model

import java.io.Serializable

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 19/02/2019
 * ************************************************************
 */
class Address(
    var comment: String?,
    var address_line: String?,
    var zip_code: String?,
    var city: Location?,
    var state: Location?,
    var country: Location?,
    var latitude: String?,
    var longitude: String?,
    var id: String?
) : Serializable {
    inner class Location(
        var id: String?,
        var name: String?
    ) : Serializable
}