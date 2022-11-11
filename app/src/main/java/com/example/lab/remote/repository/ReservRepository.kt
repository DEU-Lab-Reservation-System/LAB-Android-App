package com.example.lab.remote.repository

import com.example.lab.data.responseDto.ReservResponseDto
import com.example.lab.remote.RetrofitClient
import com.example.lab.remote.service.ReservService
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

object ReservRepository {
    private val reservService = RetrofitClient.retrofit.create(ReservService::class.java)

    /**
     * 유저의 예약 내역(종료되지 않은)을 가져오는 메소드
     */
    fun getUserReservs(userId:String):Response<ReservResponseDto.ReservList>? {
        val reservCall: Call<ReservResponseDto.ReservList> = reservService.getUserReservs(userId)

        return try{
            reservCall.execute()
        }catch (e : IOException) {
            e.printStackTrace()
            null
        }
    }
}