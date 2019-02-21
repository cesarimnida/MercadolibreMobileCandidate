package com.mercadolibre.mobilecandidate.viewmodel

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 21/02/2019
 * ************************************************************
 */
class StatusEvent<T>(val status: Status, val data: T? = null, val throwable: Throwable? = null) {
    enum class Status {
        SUCCESS, LOADING, ERROR
    }

    companion object {
        fun <T> success(data: T?) = StatusEvent(Status.SUCCESS, data)
        fun <T> loading() = StatusEvent<T>(Status.LOADING)
        fun <T> error(throwable: Throwable?) = StatusEvent<T>(Status.ERROR, null, throwable)
    }
}