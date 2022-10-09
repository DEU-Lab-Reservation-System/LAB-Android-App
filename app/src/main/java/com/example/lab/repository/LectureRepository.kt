package com.example.lab.repository

import com.example.lab.data.entity.Lecture
import com.example.lab.data.remote.RetrofitClient
import com.example.lab.data.remote.service.LectureService
import com.example.lab.data.requestDto.LectureRequestDto
import com.example.lab.data.responseDto.LectureResponseDto
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

    /**
     * 시간표 삭제 메소드
     */
    fun deleteLecture(classCode:String): Response<LectureResponseDto.Delete>?{
        val lectureCall:Call<LectureResponseDto.Delete> = lectureService.deleteLecture(classCode)

        return try{
            lectureCall.execute()
        } catch (e : IOException){
            e.printStackTrace()
            null
        }
    }

    /**
     * 전체 시간표 조회 메소드
     */
    fun getLectures():Response<ArrayList<Lecture>>?{
        val lectureCall:Call<ArrayList<Lecture>> = lectureService.getLectures()

        return try{
            lectureCall.execute()
        } catch (e : IOException){
            e.printStackTrace()
            null
        }
    }

}