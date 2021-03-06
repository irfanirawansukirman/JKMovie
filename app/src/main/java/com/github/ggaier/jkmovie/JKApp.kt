package com.github.ggaier.jkmovie

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.github.ggaier.jkmovie.di.Injections
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

/**
 * Created by ggaier
 * jwenbo52@gmail.com
 */
class JKApp : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        Injections.init(this)
        Logger.addLogAdapter(AndroidLogAdapter())
    }


}