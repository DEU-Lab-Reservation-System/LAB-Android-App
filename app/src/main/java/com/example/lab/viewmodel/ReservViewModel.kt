package com.example.lab.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab.data.requestDto.ReservRequestDto
import com.example.lab.data.responseDto.MessageDto
import com.example.lab.data.responseDto.ReservResponseDto
import com.example.lab.remote.repository.ReservRepository
import com.example.lab.utils.Event
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.streams.toList

@OptIn(DelicateCoroutinesApi::class)
class ReservViewModel : ViewModel(){
    var reserv              = MutableLiveData<ReservResponseDto.Reserv>()                // 예약 신청
    var reservList          = MutableLiveData<ReservResponseDto.ReservList>()        // 예약 조회
    var unauthReservList    = MutableLiveData<ReservResponseDto.ReservList>()  // 예약 신청 내역 조회
    var userListInLab       = MutableLiveData<ReservResponseDto.LabUserList>()        // 실습실 이용자 리스트

    var authResult          = MutableLiveData<MessageDto>()
    var rejectResult        = MutableLiveData<MessageDto>()
    var reservError         = MutableLiveData<Event<String>>() // 예약 신청 에러
    var userListError       = MutableLiveData<String>()
    /**
     * 예약 신청 메소드
     */
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
                    val errorMessage = it.errorBody()?.string()?.let { res -> JSONObject(res) }

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
                    val errorMessage = it.errorBody()?.string()?.let { res -> JSONObject(res) }

                    Log.i("예약 신청 리스트 조회 실패", "${it.code()}, ${errorMessage?.getString("message")?:""} ")
                }
            }
        }
    }

    /**
     * 예약 승인 메소드
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun authReservs(reservs:ArrayList<ReservResponseDto.Reserv>, authFlag:Boolean){
        if(reservs.isEmpty()) return

        val auth = ReservRequestDto.Auth(
            reservs.stream().map { it.id }.toList() as ArrayList<Int>,
            reservs[0].roomNumber,
            authFlag
        )

        GlobalScope.launch(Dispatchers.IO){
            val response = ReservRepository.authReservs(auth)

            response?.let {
                if(it.isSuccessful){
                    authResult.postValue(it.body())
                    Log.i("예약 승인 or 거절 완료", it.body().toString())
                } else {
                    val errorMessage = it.errorBody()?.string()?.let { res -> JSONObject(res) }

                    Log.i("예약 승인 or 거절 오류", "${it.code()}, ${errorMessage?.getString("message")?:""} ")
                }
            }
        }
    }

    /**
     * 특정 시간대에 실습실을 이용 중인 사용자를 조회하는 메소드
     */
    fun getUserListInLab(labInfo:ReservRequestDto.LabInfo){
        GlobalScope.launch(Dispatchers.IO){
            val response = ReservRepository.getUserListInLabs(labInfo)

            response?.let {
                if(it.isSuccessful){
                    userListInLab.postValue(it.body())
                    Log.i("이용자 조회 성공", it.body().toString())
                } else {
                    val errorMessage = it.errorBody()?.string()?.let { res -> JSONObject(res) }
                    userListError.postValue(errorMessage?.getString("message")?:"")
                    
                    Log.i("예약 승인 or 거절 오류", "${it.code()}, ${errorMessage?.getString("message")?:""} ")
                }
            }
        }
    }

}