package com.example.lab.data.remote.service

import com.example.lab.data.entity.Lecture
import com.example.lab.data.requestDto.LectureRequestDto
import com.example.lab.data.responseDto.LectureResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LectureService {

    /**
     * 수업 추가
     */
    @POST("api/lecture")
    fun addLecture(@Body lecture:List<LectureRequestDto.Create>): Call<List<Lecture>>

    /**
     * 전체 수업(시간표) 조회
     */
    @GET("api/lectures")
    fun getLectures(): Call<ArrayList<Lecture>>
}