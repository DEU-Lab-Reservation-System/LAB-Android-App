package com.example.lab.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.lab.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashLogo(3L)
    }

    private fun splashLogo(sec: Long){
        Handler().postDelayed(Runnable {
            val intent = Intent(applicationContext, IntroActivity::class.java)

            startActivity(intent) //intent 에 명시된 액티비티로 이동
            finish() //현재 액티비티 종료
        }, 1000L * sec)
    }
}