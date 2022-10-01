package com.example.lab.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab.data.entity.Member
import com.example.lab.data.remote.RetrofitClient
import com.example.lab.data.remote.service.MemberService
import com.example.lab.repository.MemberRepository
import com.example.lab.utils.Event
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executors

class LoginViewModel: ViewModel() {
    val loginUser = MutableLiveData<String>()
    val error = MutableLiveData<Event<String>>()

    fun login(id: String, password: String){


        Executors.newSingleThreadExecutor().execute{
            val response = MemberRepository.login(id, password)

            if(response!!.isSuccessful){
                val member = response.body()

                loginUser.postValue(member!!.userId)
                Log.i("로그인 성공", response.body().toString())
            }
            else {
                error.postValue(Event("입력 값을 확인해주세요."))

                Log.e("로그인 실패 Code", "${response.code()}")
                Log.e("로그인 실패 Message", response.message())
                Log.e("로그인 실패 ErroyBody", response.errorBody().toString())
            }
        }

    }
}