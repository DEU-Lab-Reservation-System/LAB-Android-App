package com.example.lab.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab.data.requestDto.ReportRequestDto
import com.example.lab.data.responseDto.MessageDto
import com.example.lab.data.responseDto.ReportResponseDto
import com.example.lab.remote.repository.ReportRepository
import com.example.lab.utils.Event
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class ReportViewModel:ViewModel() {
    val reportFlag = MutableLiveData<Event<Boolean>>()
    val reports    = MutableLiveData<ReportResponseDto.Reports>()

    var reportError:String?=null
    var reportsError = MutableLiveData<String>()
    /**
     * 문의(신고)를 접수하는 메소드
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun sendReport(send:ReportRequestDto.Send){
        GlobalScope.launch(Dispatchers.IO){
            val response = ReportRepository.sendReport(send)

            response?.let {
                // 통신 결과를 MessageDto의 result에 대입
                if(it.isSuccessful){
                    reportFlag.postValue(Event(true))

                    Log.i("문의 접수 완료", it.body().toString())
                } else {
                    reportFlag.postValue(Event(false))

                    val errorMessage = it.errorBody()?.string()?.let { res -> JSONObject(res) }
                    reportError = errorMessage?.getString("message")?:"오류가 발생했습니다."

                    Log.i("문의 접수 실패", "${it.code()}, $reportError")
                }
            }
        }
    }

    /**
     * 전체 문의 조회 메소드
     */
    fun getAllReport(){
        GlobalScope.launch(Dispatchers.IO){
            val response = ReportRepository.getAllReport()

            response?.let {
                if(it.isSuccessful){
                    reports.postValue(it.body())

                    Log.i("전체 문의 조회 완료", it.body().toString())
                } else {

                    val errorMessage = it.errorBody()?.string()?.let { res -> JSONObject(res) }
                    reportsError.postValue(errorMessage?.getString("message")?:"오류가 발생했습니다.")

                    Log.i("전체 문의 조회 실패", "${it.code()}, $reportError")
                }
            }
        }
    }

}