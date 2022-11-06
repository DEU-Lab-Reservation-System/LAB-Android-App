package com.example.lab.repository

import android.os.Message
import com.example.lab.data.entity.Lecture
import com.example.lab.data.remote.RetrofitClient
import com.example.lab.data.remote.service.LectureService
import com.example.lab.data.requestDto.LectureRequestDto
import com.example.lab.data.responseDto.LectureResponseDto
import com.example.lab.data.responseDto.MessageDto
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

object LectureRepository {
    private val lectureService:LectureService = RetrofitClient.retrofit.create(LectureService::class.java)

    /**
     *  시간표(수업) 추가 메소드
     */
    fun addLecture(lectureList:List<LectureRequestDto.Create>): Response<ArrayList<Lecture>>?{
        val lectureCall:Call<ArrayList<Lecture>> = lectureService.addLecture(lectureList)

        return try{
            lectureCall.execute()
        } catch (e : IOException){
            e.printStackTrace()
            null
        }
    }

    /**
     * 시간표(수업) 수정 메소드
     */
    fun editLecture(lectureList:List<LectureRequestDto.Edit>) :Response<ArrayList<Lecture>>?{
        val lectureCall:Call<ArrayList<Lecture>> = lectureService.editLecture(lectureList)

        return try{
            lectureCall.execute()
        }catch (e : IOException){
            e.printStackTrace()
            null
        }
    }

    /**
     * 시간표 삭제 메소드
     */
    fun deleteLecture(classCode:String): Response<MessageDto>?{
        val lectureCall:Call<MessageDto> = lectureService.deleteLecture(classCode)

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