package com.mzhadan.catsapp

import android.app.Application
import com.mzhadan.catsapp.di.AppComponent
import com.mzhadan.catsapp.di.DaggerAppComponent


class App: Application() {

    companion object{
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder().build()
    }
}