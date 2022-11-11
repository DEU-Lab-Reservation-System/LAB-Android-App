package com.example.lab.remote.service

import com.example.lab.data.responseDto.ReservResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReservService {
    @POST("api/reservation")
    fun addReserv()

    @GET("api/reservations/{userId}")
    fun getUserReservs(@Path("userId") userId:String): Call<ReservResponseDto.ReservList>

    @GET("api/reservations/unauthorized")
    fun getUnauthReservs(): Call<ReservResponseDto.ReservList>
}