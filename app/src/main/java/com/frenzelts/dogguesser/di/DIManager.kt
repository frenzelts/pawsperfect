package com.frenzelts.dogguesser.di

object DIManager {
    private val _appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().build()
    }

    fun getAppComponent(): AppComponent = _appComponent
}