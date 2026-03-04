package com.lucwaw.androidapp

import android.app.Application
import kmp.project.gameoflife.di.initKoin
import org.koin.android.ext.koin.androidContext

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@AndroidApp)
        }
    }
}
