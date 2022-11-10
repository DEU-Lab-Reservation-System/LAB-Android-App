package com.example.lab.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab.data.responseDto.LabResponseDto
import com.example.lab.remote.repository.LabRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(DelicateCoroutinesApi::class)
class LabViewModel : ViewModel() {
    var labStatus = MutableLiveData<LabResponseDto.Status>()

    fun getLabStatus(labNumber:Int){
        GlobalScope.launch(Dispatchers.IO) {
            val response = LabRepository.getLabStatus(labNumber)

            if(response!!.isSuccessful){
                labStatus.postValue(response.body())
                
                Log.i("실습실 현황 조회 성공", response.body().toString())
            } else{
                val errorMessage = response.errorBody()?.string()?.let { JSONObject(it) }

                Log.i("실습실 현황 조회 실패", "${response.code()}, ${errorMessage?.getString("message")?:""} ")
            }
        }
    }

}