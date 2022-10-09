package com.example.lab.data.remote.service

import com.example.lab.data.entity.Lecture
import com.example.lab.data.requestDto.LectureRequestDto
import com.example.lab.data.responseDto.LectureResponseDto
import retrofit2.Call
import retrofit2.http.*

interface LectureService {

    /**
     * 수업 추가
     */
    @POST("api/lecture")
    fun addLecture(@Body lecture:List<LectureRequestDto.Create>): Call<List<Lecture>>

    @DELETE("api/lectures/{code}")
    fun deleteLecture(@Path("code") classCode:String): Call<LectureResponseDto.Delete>
    /**
     * 전체 수업(시간표) 조회
     */
    @GET("api/lectures")
    fun getLectures(): Call<ArrayList<Lecture>>
}