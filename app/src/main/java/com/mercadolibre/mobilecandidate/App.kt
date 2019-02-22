package com.mercadolibre.mobilecandidate

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber
import timber.log.Timber.DebugTree


/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 22/02/2019
 * ************************************************************
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)
        if (!BuildConfig.DEBUG) return
        Timber.plant(DebugTree())
    }
}