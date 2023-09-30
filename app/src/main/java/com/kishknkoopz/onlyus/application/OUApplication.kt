package com.kishknkoopz.onlyus.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OUApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        OUApplicationHelper.init(this)
    }

}