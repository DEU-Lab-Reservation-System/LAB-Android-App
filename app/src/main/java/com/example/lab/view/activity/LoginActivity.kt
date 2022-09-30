package com.example.lab.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lab.R
import com.example.lab.databinding.ActivityLoginBinding
import com.example.lab.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var bind: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.signInBtn.setOnClickListener {
            val intent = Intent(applicationContext, TokenActivity::class.java)

            startActivity(intent) //intent 에 명시된 액티비티로 이동
            finish() //현재 액티비티 종료
        }
    }
}