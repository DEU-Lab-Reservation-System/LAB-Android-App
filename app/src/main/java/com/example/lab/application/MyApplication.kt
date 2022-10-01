package com.example.lab.application

import android.app.Application
import android.content.Context
import com.example.lab.data.entity.Member

/** 전역적으로 공유해야하는 변수는 이 클래스안에서 관리 */
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object{
        private var context: Context? = null
        var member: Member? = null

        @JvmStatic
        fun ApplicationContext(): Context? {
            return context
        }
    }
}