package com.example.lab.repository

import com.example.lab.data.entity.Lecture
import com.example.lab.data.remote.RetrofitClient
import com.example.lab.data.remote.service.LectureService
import com.example.lab.data.requestDto.LectureRequestDto
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

object LectureRepository {
    private val lectureService:LectureService = RetrofitClient.retrofit.create(LectureService::class.java)

    /**
     *  시간표(수업) 추가 메소드
     */
    fun addLecture(lectureList:List<LectureRequestDto.Create>): Response<List<Lecture>>?{
        val lectureCall:Call<List<Lecture>> = lectureService.addLecture(lectureList)

        return try{
            lectureCall.execute()
        } catch (e : IOException){
            e.printStackTrace()
            null
        }
    }

}