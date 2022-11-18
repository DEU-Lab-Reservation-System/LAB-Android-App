package com.example.lab.remote.service

import com.example.lab.data.requestDto.ReportRequestDto
import com.example.lab.data.responseDto.MessageDto
import com.example.lab.data.responseDto.ReportResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ReportService {
    @POST("api/report")
    fun sendReport(@Body report:ReportRequestDto.Send): Call<MessageDto>

    @GET("api/report")
    fun getAllReport(): Call<ReportResponseDto.Reports>
}