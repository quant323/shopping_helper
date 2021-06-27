package com.zedevstuds.price_equalizer.utils

import android.app.Application

// Класс всего приложения
class App : Application() {

    companion object {
        // Для возможности исп. Context во всем приложении
        lateinit var app: App
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }

}