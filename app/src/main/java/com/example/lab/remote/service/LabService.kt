package com.example.lab.remote.service


import com.example.lab.data.responseDto.LabResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface LabService {

    @GET("api/labs/timetable")
    fun getTimetable()

    @GET("api/labs/{roomNumber}")
    fun getLabStatus(@Path("roomNumber") labNumber:Int): Call<LabResponseDto.Status>

}