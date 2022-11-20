package com.example.lab.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab.data.requestDto.TokenRequestDto
import com.example.lab.data.responseDto.TokenResponseDto
import com.example.lab.remote.repository.TokenRepository
import com.example.lab.utils.DateManager
import com.example.lab.utils.Event
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.util.*
import java.util.concurrent.Executors

class TokenViewModel : ViewModel() {
    val tokenFlag = MutableLiveData<Boolean>(false)
    val token = MutableLiveData<TokenResponseDto.Token>()
    val createError = MutableLiveData<String>()
    val error = MutableLiveData<Event<String>>()

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(DelicateCoroutinesApi::class)
    fun createToken(){
        GlobalScope.launch(Dispatchers.IO){
            val today = DateManager.getTodayUntilDate()
            val year = Calendar.getInstance().weekYear

            val expireDate = if(today > "${year}-09-01") "${year}-12-31" else "${year}-06-31"

            val response: Response<TokenResponseDto.Token>? = TokenRepository.createToken(TokenRequestDto.Create(expireDate))

            response?.let {
                if(it.isSuccessful){
                    token.postValue(it.body())

                    Log.i("토큰 발급 성공", "${it.body()}")
                } else {
                    val errorMessage = it.errorBody()?.string()?.let { res -> JSONObject(res) }
                    createError.postValue(errorMessage?.getString("message")?:"")

                    Log.i("토큰 검증 실패", "${it.code()}, ${errorMessage?.getString("message")?:""} ")
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
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