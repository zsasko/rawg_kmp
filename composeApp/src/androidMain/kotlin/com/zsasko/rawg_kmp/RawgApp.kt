package com.zsasko.rawg_kmp

import android.app.Application
import com.zsasko.rawg_kmp.di.initApplication
import org.koin.android.ext.koin.androidContext

class RawgApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initApplication {
            androidContext(this@RawgApp)
        }

    }
}