package com.example.lab.remote.repository

import com.example.lab.data.requestDto.ReservRequestDto
import com.example.lab.data.responseDto.ReservResponseDto
import com.example.lab.remote.RetrofitClient
import com.example.lab.remote.service.ReservService
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

object ReservRepository {
    private val reservService = RetrofitClient.retrofit.create(ReservService::class.java)

    /**
     * 예약 등록 메소드
     */
    fun addReservation(reservInfo:ReservRequestDto.Create):Response<ReservResponseDto.Reserv>? {
        val reservCall: Call<ReservResponseDto.Reserv> = reservService.addReserv(reservInfo)

        return try{
            reservCall.execute()
        } catch (e :IOException){
            e.printStackTrace()
            null
        }
    }

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

    /**
     * 승인되지 않은 예약(예약 신청 리스트)를 가져오는 메소드
     */
    fun getUnauthReserv():Response<ReservResponseDto.ReservList>? {
        val reservCall: Call<ReservResponseDto.ReservList> = reservService.getUnauthReservs()

        return try{
            reservCall.execute()
        } catch (e: IOException){
            e.printStackTrace()
            null
        }
    }
}