package com.example.lab.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab.data.responseDto.TokenResponseDto
import com.example.lab.remote.repository.TokenRepository
import com.example.lab.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.util.concurrent.Executors

class TokenViewModel : ViewModel() {
    val tokenFlag = MutableLiveData<Boolean>(false)
    val error = MutableLiveData<Event<String>>()

    fun checkToken(userId: String, inputToken:String){
        // 코루틴 생성
        GlobalScope.launch(Dispatchers.IO) {
            val response: Response<TokenResponseDto.Check>? = TokenRepository.checkToken(userId, inputToken)

            response?.let {
                if(it.isSuccessful && it.body()!!.auth){
                    tokenFlag.postValue(true)

                    Log.i("토큰 검증 성공", "${it.body()}")
                } else {
                    error.postValue(Event("토큰이 유효하지 않습니다."))

                    val errorMessage = it.errorBody()?.string()?.let { res -> JSONObject(res) }

                    Log.i("토큰 검증 실패", "${it.code()}, ${errorMessage?.getString("message")?:""} ")
                }
            }
        }
    }
}