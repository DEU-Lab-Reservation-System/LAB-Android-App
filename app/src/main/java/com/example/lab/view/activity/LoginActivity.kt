package com.example.lab.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.lab.R
import com.example.lab.databinding.ActivityLoginBinding
import com.example.lab.databinding.ActivityMainBinding
import com.example.lab.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    // VARIABLE
    private lateinit var bind: ActivityLoginBinding

    private lateinit var loginViewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bind.root)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        login()
    }

    private fun login(){
        /** 로그인 버튼 이벤트 등록 */
        bind.signInBtn.setOnClickListener {
            val id = bind.idEditText.editText?.text.toString()
            val pw = bind.pwEditText.editText?.text.toString()

            if(id.isNullOrEmpty()){
                bind.idEditText.error = "빈 칸 없이 기재해주세요."
                return@setOnClickListener
            }

            // 영문과 숫자가 모두 포함되고 6자리 이상인지 체크
            if(!pw.matches(Regex("(?=.*[a-zA-Z])(?=.*[0-9]).{6,}"))){
                bind.pwEditText.error = "비밀번호는 영문과 숫자 포함 6자 이상이어야합니다."
                return@setOnClickListener
            }

            loginViewModel.login(id, pw)
        }

        /** 로그인 성공 시 */
        loginViewModel.loginUser.observe(this, Observer{
            val intent = Intent(applicationContext, TokenActivity::class.java)

            startActivity(intent) //intent 에 명시된 액티비티로 이동
            finish() //현재 액티비티 종료
        })

        /** 로그인 실패 시 */
        loginViewModel.error.observe(this) {
            val errorMessage = it.contentIfNotHandled()
            bind.pwEditText.error = errorMessage
        }
    }
}