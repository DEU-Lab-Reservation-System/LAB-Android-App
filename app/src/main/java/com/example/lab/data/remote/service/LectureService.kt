package com.example.lab.data.remote.service

import com.example.lab.data.entity.Lecture
import com.example.lab.data.requestDto.LectureRequestDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LectureService {

    @POST("api/lecture")
    fun addLecture(@Body lecture:LectureRequestDto.Lecture): Call<Lecture>
}