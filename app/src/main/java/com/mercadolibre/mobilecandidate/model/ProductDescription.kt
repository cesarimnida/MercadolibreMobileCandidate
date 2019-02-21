package com.mercadolibre.mobilecandidate.model

import java.io.Serializable

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 20/02/2019
 * ************************************************************
 */
class ProductDescription(
    var text: String?,
    var plain_text: String?,
    var last_updated: String?,
    var date_created: String?
) : Serializable