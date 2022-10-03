package com.example.lab.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.lab.data.entity.Lecture
import com.example.lab.data.requestDto.LectureRequestDto
import com.example.lab.repository.LectureRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.streams.toList

class LectureViewModel : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.N)
    fun addLecture(lectureList:ArrayList<Lecture>){
        // dto 리스트로 변환
        val lectureDtoList = lectureList.stream().map { lecture ->
            LectureRequestDto.Create.createDto(lecture)
        }.toList()

        GlobalScope.launch {
            val response = LectureRepository.addLecture(lectureDtoList)

            if(response!!.isSuccessful){
                Log.i("수업 추가 완료", response.body().toString())
            }
            else{
                val errorMessage = JSONObject(response.errorBody()?.string())

                Log.i("수업 추가 실패", "${response.code()}")
                Log.i("수업 추가 실패", errorMessage.getString("message"))
            }
        }
    }
}