package com.example.lab.remote.repository

import com.example.lab.data.requestDto.ReportRequestDto
import com.example.lab.data.responseDto.MessageDto
import com.example.lab.data.responseDto.ReportResponseDto
import com.example.lab.remote.RetrofitClient
import com.example.lab.remote.service.ReportService
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

object ReportRepository {
    private val reportService = RetrofitClient.retrofit.create(ReportService::class.java)

    /**
     * 문의(신고)를 접수하는 메소드
     */
    fun sendReport(report:ReportRequestDto.Send):Response<MessageDto>? {
        val reportCall: Call<MessageDto> = reportService.sendReport(report)

        return try{
            reportCall.execute()
        } catch (e : IOException){
            e.printStackTrace()
            null
        }
    }

    /**
     * 전체 문의(신고)를 조회하는 메소드
     */
    fun getAllReport(): Response<ReportResponseDto.Reports>?{
        val reportCall: Call<ReportResponseDto.Reports> = reportService.getAllReport()

        return try{
            reportCall.execute()
        } catch (e : IOException){
            e.printStackTrace()
            null
        }
    }
}