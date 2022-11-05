package com.example.lab.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab.data.entity.Lecture
import com.example.lab.data.requestDto.LectureRequestDto
import com.example.lab.repository.LectureRepository
import com.example.lab.utils.ResponseLogger
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.util.stream.Collectors
import kotlin.streams.toList


@OptIn(DelicateCoroutinesApi::class)
class LectureViewModel : ViewModel() {

    val lectureHash = MutableLiveData<HashMap<String, ArrayList<Lecture>>>()
    val addLectureFlag = MutableLiveData<Boolean>()

    /**
     * 수업 추가 메소드
     * @param lectureList ArrayList<Lecture>
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun addLecture(lectureList:ArrayList<Lecture>){
        // dto 리스트로 변환
        val lectureDtoList = lectureList.stream().map { lecture ->
            LectureRequestDto.Create.createDto(lecture)
        }.toList()

        GlobalScope.launch(Dispatchers.IO) {
            val response = LectureRepository.addLecture(lectureDtoList)

            if(response!!.isSuccessful){
                // 새로 추가한 수업은 해시에 추가
                hashingLectures(response.body() as ArrayList<Lecture>)
                addLectureFlag.postValue(true)
                Log.i("수업 추가 완료", response.body().toString())
            }
            else{
                addLectureFlag.postValue(false)

                val errorMessage = response.errorBody()?.string()?.let { JSONObject(it) }

                Log.i("수업 추가 실패", "${response.code()}")
                Log.i("수업 추가 실패", errorMessage?.getString("message") ?: "")
            }
        }
    }

    /**
     * 수업 삭제 메소드
     * @param classCode String
     */
    fun deleteLecture(classCode: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = LectureRepository.deleteLecture(classCode)

            if (response!!.isSuccessful) {
                Log.i("수업 삭제 완료", classCode)
            } else {
                val errorMessage = response.errorBody()?.string()?.let { JSONObject(it) }

                Log.i("수업 삭제 실패", "${response.code()}")
                Log.i("수업 삭제 실패", errorMessage?.getString("message") ?: "")
            }
        }
    }

    /**
     * 전체 시간표 조회 메소드
     */
    fun getAllLectures(){
        GlobalScope.launch(Dispatchers.IO) {
            val response = LectureRepository.getLectures()

            if(response!!.isSuccessful){
                response.body()?.let {
                    // 서버에서 받은 시간표 리스트를 해시에 넣어서 저장
                    lectureHash.postValue(hashingLectures(it))

                    Log.i("전체 시간표 조회 완료", response.body().toString())
                }
            }
            else{
                val errorMessage = response.errorBody()?.string()?.let { JSONObject(it) }

                Log.i("전체 시간표 조회 실패", "${response.code()}")
                Log.i("전체 시간표 조회 실패", errorMessage?.getString("message") ?: "")
            }
        }
    }

    /**
     * 특정 강의실의 시간표(수업)을 가져오는 메소드
     * @param labId String
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun getLabsLectures(labId:String):Map<String?, List<Lecture>> {
        val lectures = ArrayList<Lecture>()

        lectureHash.value?.entries?.forEach {
            it.value.forEach { lecture ->
                if(lecture.place == labId) lectures.add(lecture)
            }
        }

        return lectures.stream().collect(Collectors.groupingBy(Lecture::title))
    }

    /**
     * 수업 코드로 수업을 찾는 메소드
     */
    fun getLectures(code:String):ArrayList<Lecture>? {
        return lectureHash.value?.get(code)
    }

    /**
     * 시간표를 해시에 추가
     *
     * code (강의 코드)를 Key, ArrayList<Lecture>를 Value로 가짐
     * @param lectureList ArrayList<Lecture>
     */
    private fun hashingLectures(lectureList: ArrayList<Lecture>):HashMap<String, ArrayList<Lecture>> {
        val lectureHash = HashMap<String, ArrayList<Lecture>>()

        lectureList.forEach { it ->
            if(lectureHash.containsKey(it.code)) lectureHash[it.code]?.add(it)
            else {
                it.code?.let { code ->
                    lectureHash[code] = arrayListOf(it)
                }
            }
        }

        return lectureHash
    }

}