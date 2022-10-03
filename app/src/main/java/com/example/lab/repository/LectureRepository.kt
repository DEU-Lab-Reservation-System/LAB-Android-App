package com.example.lab.repository

import com.example.lab.data.entity.Lecture
import com.example.lab.data.remote.RetrofitClient
import com.example.lab.data.remote.service.LectureService
import retrofit2.Retrofit
import retrofit2.create

object LectureRepository {
    private val lectureService:LectureService = RetrofitClient.retrofit.create(LectureService::class.java)

    /**
     *  시간표(수업) 추가 메소드
     */
    fun addLecture(lectureList:List<Lecture>){
    }

}