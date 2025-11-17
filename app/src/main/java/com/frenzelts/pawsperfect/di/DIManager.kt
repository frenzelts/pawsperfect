package com.frenzelts.pawsperfect.di

import android.app.Application

object DIManager {

    fun createAppComponent(application: Application): AppComponent {
        return DaggerAppComponent
            .builder()
            .application(application)
            .build()
    }

}