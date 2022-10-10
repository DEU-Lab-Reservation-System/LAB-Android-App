package com.example.lab.view.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.lab.R
import com.example.lab.application.MyApplication
import com.example.lab.databinding.ActivityLoginBinding
import com.example.lab.view.fragment.SignUpFragment
import com.example.lab.viewmodel.MemberViewModel

class LoginActivity : AppCompatActivity() {

    // VARIABLE
    private lateinit var bind: ActivityLoginBinding
    private lateinit var memberVM: MemberViewModel


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bind.root)

        memberVM = ViewModelProvider(this)[MemberViewModel::class.java]

        addLoginBtnEvent()
        addSignUpBtnEvent()
    }

    /** 회원가입 버튼 이벤트 등록 */
    private fun addSignUpBtnEvent(){
        bind.signUpBtn.setOnClickListener{
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.parentLayout, SignUpFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    /** 로그인 버튼 이벤트 등록 */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addLoginBtnEvent(){
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

            memberVM.login(id, pw)
        }

        /** 로그인 성공 시 */
        memberVM.loginFlag.observe(this, Observer{
            // 인증이 되지 않은 사용자면 토큰 입력 화면으로 이동
            MyApplication.member?.let {
                val intent = if(!it.isAuth){
                    Intent(applicationContext, TokenActivity::class.java)
                } else {
                    Intent(applicationContext, MainActivity::class.java)
                }

                startActivity(intent) //intent 에 명시된 액티비티로 이동
                finish() //현재 액티비티 종료
            }
        })

        /** 로그인 실패 시 */
        memberVM.loginError.observe(this) {
            bind.pwEditText.error = it.contentIfNotHandled()
        }
    }


    /** 뒤로가기 버튼 클릭 이벤트 */
    private val limitTime = 1000        // 뒤로가기 버튼 누르는 제한시간
    private var pressTime:Long = 0L     // 누른 시점

    override fun onBackPressed() {
        val now = System.currentTimeMillis()
        val interval = now - pressTime

        if(interval in 0..limitTime) {
            finish()
        }
        else{
            pressTime = now
            Toast.makeText(applicationContext, "한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyDown(keyCode, event)
    }
}