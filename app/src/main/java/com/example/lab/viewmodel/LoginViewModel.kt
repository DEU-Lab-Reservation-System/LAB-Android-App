package com.example.lab.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab.application.MyApplication
import com.example.lab.repository.MemberRepository
import com.example.lab.utils.Event
import org.json.JSONObject
import java.util.concurrent.Executors

class LoginViewModel: ViewModel() {
    val loginFlag = MutableLiveData<Boolean>(false)
    val error = MutableLiveData<Event<String>>()

    fun login(id: String, password: String){
        Executors.newSingleThreadExecutor().execute{
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

                error.postValue(Event(errorMessage.getString("message")))

                Log.e("로그인 실패 Code", "${response.code()}")
                Log.e("로그인 실패 Message", errorMessage.getString("message"))
            }
        }

    }
}