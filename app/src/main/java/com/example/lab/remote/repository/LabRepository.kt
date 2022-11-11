package com.example.lab.remote.repository

import com.example.lab.data.requestDto.LabRequestDto
import com.example.lab.remote.RetrofitClient
import com.example.lab.remote.service.LabService
import com.example.lab.data.responseDto.LabResponseDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

object LabRepository {
    private val labService: LabService = RetrofitClient.retrofit.create(LabService::class.java)

    fun getLabStatus(labNumber:Int, timeRange: LabRequestDto.TimeRange?): Response<LabResponseDto.Status>? {
        val labCall:Call<LabResponseDto.Status> =
            if(timeRange == null) labService.getLabStatus(labNumber)
            else labService.getLabStatusInTime(labNumber, timeRange)

        return try{
            labCall.execute()
        }catch (e: IOException){
            e.printStackTrace()
            null
        }
    }
}