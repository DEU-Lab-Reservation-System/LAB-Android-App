package com.example.lab.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab.data.responseDto.ReservResponseDto
import com.example.lab.remote.repository.ReservRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(DelicateCoroutinesApi::class)
class ReservViewModel : ViewModel(){
    var reservList = MutableLiveData<ReservResponseDto.ReservList>()

    fun getUserReservs(userId:String){
        GlobalScope.launch(Dispatchers.IO) {
            val response = ReservRepository.getUserReservs(userId)

            response?.let {
                if(it.isSuccessful){
                    reservList.postValue(it.body())
                    Log.i("예약 조회 완료", it.body().toString())
                }else{
                    val errorMessage = response.errorBody()?.string()?.let { res -> JSONObject(res) }

                    Log.i("예약 조회 실패", "${it.code()}, ${errorMessage?.getString("message")?:""} ")
                }
            }

        }
    }
}