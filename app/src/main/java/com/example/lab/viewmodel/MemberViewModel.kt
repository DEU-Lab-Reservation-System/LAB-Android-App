package com.example.lab.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab.application.MyApplication
import com.example.lab.data.requestDto.MemberRequestDto
import com.example.lab.remote.repository.MemberRepository
import com.example.lab.utils.Event
import com.example.lab.utils.ResponseLogger
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.concurrent.Executors

class MemberViewModel: ViewModel() {
    val loginFlag = MutableLiveData<Boolean>(false)
    val signUpFlag = MutableLiveData<Boolean>(false)
    val idCheckFlag = MutableLiveData<Boolean>(false)

    val loginError = MutableLiveData<Event<String>>()
    val signUpError = MutableLiveData<Event<String>>()
    val idCheckError = MutableLiveData<Event<String>>()



    /**
     * 로그인 메소드
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun login(id: String, password: String){
        GlobalScope.launch(Dispatchers.IO) {
            val response = MemberRepository.login(id, password, MyApplication.deviceToken!!)

            if(response!!.isSuccessful){
                val member = response.body()

                // 전역 변수로 저장
                MyApplication.member = member

                loginFlag.postValue(true)
                Log.i("로그인 성공", response.body().toString())
            }
            else {
                val errorMessage = JSONObject(response.errorBody()?.string()!!)

//                ResponseLogger.loggingError("로그인 실패", response)

                loginError.postValue(Event(errorMessage.getString("message")?:""))

                Log.e("로그인 실패 Code", "${response.code()}")
                Log.e("로그인 실패 Message", errorMessage.getString("message")?:"")
            }
        }
    }

    /**
     * 회원가입 메소드
     */
    fun signUp(signUpDto: MemberRequestDto.SignUp){
        GlobalScope.launch(Dispatchers.IO){
            val response = MemberRepository.signUp(signUpDto)

            if(response!!.isSuccessful){
                signUpFlag.postValue(true)
                Log.i("회원 가입 완료", response.body().toString())
            } else {
                val errorMessage = JSONObject(response.errorBody()?.string()!!)

                signUpError.postValue(Event(errorMessage.getString("message")?:""))

                Log.e("회원가입 실패 Code", "${response.code()}")
                Log.e("회원가입 실패 Message", errorMessage.getString("message")?:"")
            }
        }
    }

    /**
     * 아이디 중복 확인 메소드
     */
    fun idCheck(id:String){
        GlobalScope.launch(Dispatchers.IO) {
            val response = MemberRepository.idCheck(id)

            if(response!!.isSuccessful){
                idCheckFlag.postValue(true)
                Log.i("아이디 중복 확인 성공", response.body().toString())
            } else {
                val errorMessage = JSONObject(response.errorBody()?.string()!!)

                idCheckError.postValue(Event(errorMessage.getString("message")?:""))

                Log.e("아이디 중복 확인 실패 Code", "${response.code()}")
                Log.e("아이디 중복 확인 실패 Message", errorMessage.getString("message")?:"")
            }
        }
    }
}