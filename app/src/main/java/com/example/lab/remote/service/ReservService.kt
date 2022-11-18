package com.example.lab.remote.service

import android.os.Message
import com.example.lab.data.requestDto.ReservRequestDto
import com.example.lab.data.responseDto.MessageDto
import com.example.lab.data.responseDto.ReservResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReservService {
    @POST("api/reservation")
    fun addReserv(@Body reservInfo:ReservRequestDto.Create): Call<ReservResponseDto.Reserv>

    @GET("api/reservations/{userId}")
    fun getUserReservs(@Path("userId") userId:String): Call<ReservResponseDto.ReservList>

    @GET("api/reservations/unauthorized")
    fun getUnauthReservs(): Call<ReservResponseDto.ReservList>

    /**
     * 예약 승인 or 거절 요청 메소드
     */
    @POST("api/reservations/authorize")
    fun authReservs(@Body auth:ReservRequestDto.Auth):Call<MessageDto>

    @POST("api/reservations/list")
    fun getUserListInLab(@Body labInfo: ReservRequestDto.LabInfo): Call<ReservResponseDto.LabUserList>

    @DELETE("api/reservations/{reservationId}")
    fun cancleReserv(@Path("reservationId") reservId:Int): Call<MessageDto>

    @POST("api/reservations/extend")
    fun extendReserv(@Body extend: ReservRequestDto.Extend): Call<ReservResponseDto.Reserv>
}