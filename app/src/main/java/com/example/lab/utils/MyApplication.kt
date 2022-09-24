package com.example.lab.utils

import android.app.Application
import android.content.Context

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object{
        private var context: Context? = null

        @JvmStatic
        fun ApplicationContext(): Context? {
            return context
        }
    }
}