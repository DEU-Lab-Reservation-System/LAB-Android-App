package com.example.lab.remote.service


import com.example.lab.data.requestDto.LabRequestDto
import com.example.lab.data.responseDto.LabResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LabService {

    @GET("api/labs/timetable")
    fun getTimetable()

    @GET("api/labs/{roomNumber}")
    fun getLabStatus(@Path("roomNumber") labNumber:Int): Call<LabResponseDto.Status>

    @POST("api/labs/{roomNumber}")
    fun getLabStatusInTime(@Path("roomNumber") labNumber: Int, @Body timeRange: LabRequestDto.TimeRange): Call<LabResponseDto.Status>
}