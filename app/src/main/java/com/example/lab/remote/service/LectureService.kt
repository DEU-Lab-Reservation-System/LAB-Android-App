package com.example.lab.remote.service

import com.example.lab.data.entity.Lecture
import com.example.lab.data.requestDto.LectureRequestDto
import com.example.lab.data.responseDto.LectureResponseDto
import com.example.lab.data.responseDto.MessageDto
import retrofit2.Call
import retrofit2.http.*

interface LectureService {

    /**
     * 수업 추가
     */
    @POST("api/lecture")
    fun addLecture(@Body lecture:List<LectureRequestDto.Create>): Call<ArrayList<Lecture>>

    @PUT("api/lectures")
    fun editLecture(@Body lecture:List<LectureRequestDto.Edit>): Call<ArrayList<Lecture>>

    @DELETE("api/lectures/{code}")
    fun deleteLecture(@Path("code") classCode:String): Call<MessageDto>

    /**
     * 전체 수업(시간표) 조회
     */
    @GET("api/lectures")
    fun getLectures(): Call<ArrayList<Lecture>>
}