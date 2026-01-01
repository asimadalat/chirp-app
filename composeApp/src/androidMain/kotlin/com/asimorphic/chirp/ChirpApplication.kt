package com.asimorphic.chirp

import android.app.Application
import com.asimorphic.chirp.dependency_injection.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class ChirpApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(androidContext = this@ChirpApplication)
            androidLogger()
        }
    }
}