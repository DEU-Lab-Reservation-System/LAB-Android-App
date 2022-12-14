package com.example.lab.remote.repository

import android.os.Message
import com.example.lab.data.requestDto.ReservRequestDto
import com.example.lab.data.responseDto.MessageDto
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

    /**
     * 예약 승인 or 거절 요청 메소드
     */
    fun authReservs(auth:ReservRequestDto.Auth):Response<MessageDto>? {
        val reservCall:Call<MessageDto> = reservService.authReservs(auth)

        return try{
            reservCall.execute()
        } catch (e: IOException){
            e.printStackTrace()
            null
        }
    }

    /**
     * 특정 시간대에 실습실을 이용 중인 사용자 목록을 가져오는 메소드
     */
    fun getUserListInLabs(labInfo:ReservRequestDto.LabInfo): Response<ReservResponseDto.LabUserList>?{
        val reservCall:Call<ReservResponseDto.LabUserList> = reservService.getUserListInLab(labInfo)

        return try{
            reservCall.execute()
        } catch (e: IOException){
            e.printStackTrace()
            null
        }
    }
    /**
     * 예약 취소 메소드
     */
    fun cancleReserv(reservId:Int): Response<MessageDto>? {
        val reservCall: Call<MessageDto> = reservService.cancleReserv(reservId)

        return try{
            reservCall.execute()
        } catch (e: IOException){
            e.printStackTrace()
            null
        }
    }

    /**
     * 예약 연장 메소드
     */
    fun extendReserv(extend:ReservRequestDto.Extend): Response<ReservResponseDto.Reserv>?{
        val reservCall: Call<ReservResponseDto.Reserv> = reservService.extendReserv(extend)

        return try{
            reservCall.execute()
        } catch (e: IOException){
            e.printStackTrace()
            null
        }
    }
}