package com.pss.spogoo.util

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(applicationContext)

    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}