package com.kishknkoopz.onlyus.application

import android.content.Context

object OUApplicationHelper {
    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun getAppContext(): Context {
        return appContext ?: throw IllegalStateException("Context not initialized")
    }
}