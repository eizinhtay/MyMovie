package com.example.mymovie

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyMovieApp : Application() {

    override fun onCreate() {
        super.onCreate()

    }
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }


}