package com.example.lab.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab.data.entity.Lecture
import com.example.lab.data.requestDto.LectureRequestDto
import com.example.lab.repository.LectureRepository
import com.example.lab.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.streams.toList


class LectureViewModel : ViewModel() {

    val lectureList:MutableLiveData<ArrayList<Lecture>?> = MutableLiveData<ArrayList<Lecture>?>()


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

    /**
     * 전체 시간표 조회 메소드
     * @return ArrayList<Lecture>
     */
    fun getLectures(){
        GlobalScope.launch(Dispatchers.IO) {
            val response = LectureRepository.getLectures()

            if(response!!.isSuccessful){
                lectureList.postValue(response.body())
                Log.i("전체 시간표 조회 완료", response.body().toString())
            }
            else{
                val errorMessage = JSONObject(response.errorBody()?.string())

                Log.i("전체 시간표 조회 실패", "${response.code()}")
                Log.i("전체 시간표 조회 실패", errorMessage.getString("message"))
            }
        }
    }
}