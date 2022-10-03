package com.example.lab.application

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.lab.data.entity.Member
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

/** 전역적으로 공유해야하는 변수는 이 클래스안에서 관리 */
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        // 파이어베이스 인스턴스 초기화 (파이어베이스 이용 시 필수)
        FirebaseApp.initializeApp(this)
        getDeviceToken()
    }

    /** FCM 알림을 위한 디바이스 토큰을 가져오는 메소드 */
    private fun getDeviceToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener{
                task ->
            // 해당 조건문이 없으면 task is not yet complete 에러 발생
            if(task.isSuccessful){
                deviceToken = task.result ?: ""

                Log.i("디바이스 토큰", deviceToken.toString())
            }
        }
    }

    companion object{
        private var context: Context? = null
        /** 사용자 정보 클래스 */
        var member: Member? = null

        /** 디바이스 토큰 */
        var deviceToken: String? = null

        @JvmStatic
        fun ApplicationContext(): Context? {
            return context
        }
    }
}