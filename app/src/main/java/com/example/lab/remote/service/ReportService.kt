package com.example.lab.remote.service

import com.example.lab.data.requestDto.ReportRequestDto
import com.example.lab.data.responseDto.MessageDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportService {
    @POST("api/report")
    fun sendReport(@Body report:ReportRequestDto.Send): Call<MessageDto>
}