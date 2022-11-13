package com.example.lab.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab.data.requestDto.ReportRequestDto
import com.example.lab.data.responseDto.MessageDto
import com.example.lab.remote.repository.ReportRepository
import com.example.lab.utils.Event
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class ReportViewModel:ViewModel() {
    val reportFlag = MutableLiveData<Event<Boolean>>()
    var reportError:String?=null

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

}