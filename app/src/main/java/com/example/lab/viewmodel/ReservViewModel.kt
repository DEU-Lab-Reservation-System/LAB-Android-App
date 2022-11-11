package com.example.lab.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab.data.requestDto.ReservRequestDto
import com.example.lab.data.responseDto.ReservResponseDto
import com.example.lab.remote.repository.ReservRepository
import com.example.lab.utils.Event
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(DelicateCoroutinesApi::class)
class ReservViewModel : ViewModel(){
    var reserv = MutableLiveData<ReservResponseDto.Reserv>()                // 예약 신청
    var reservList = MutableLiveData<ReservResponseDto.ReservList>()        // 예약 조회
    var unauthReservList = MutableLiveData<ReservResponseDto.ReservList>()  // 예약 신청 내역 조회

    var reservError = MutableLiveData<Event<String>>() // 예약 신청 에러

    fun addReservation(reservInfo:ReservRequestDto.Create){
        GlobalScope.launch(Dispatchers.IO){
            val response = ReservRepository.addReservation(reservInfo)

            response?.let {
                if(it.isSuccessful){
                    reserv.postValue(it.body())
                    Log.i("예약 신청 완료", it.body().toString())
                } else {
                    val errorMessage = it.errorBody()?.string()?.let { res -> JSONObject(res) }

                    reservError.postValue(Event(errorMessage?.getString("message")?:""))
                    Log.i("예약 신청 실패", "${it.code()}, ${errorMessage?.getString("message")?:""} ")
                }
            }
        }
    }

    /**
     * 사용자의 예약 내역을 가져오는 메소드
     */
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

    /**
     * 승인 되지 않은 예약(예약 신청 리스트)를 가져오는 메소드
     */
    fun getUnauthReservs(){
        GlobalScope.launch(Dispatchers.IO){
            val response = ReservRepository.getUnauthReserv()

            response?.let {
                if(it.isSuccessful){
                    unauthReservList.postValue(it.body())
                    Log.i("예약 신청 리스트 조회 완료", it.body().toString())
                } else {
                    val errorMessage = response.errorBody()?.string()?.let { res -> JSONObject(res) }

                    Log.i("예약 신청 리스트 조회 실패", "${it.code()}, ${errorMessage?.getString("message")?:""} ")
                }
            }
        }
    }
}