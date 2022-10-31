package com.example.lab.utils

import android.util.Log
import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import retrofit2.Response

object ResponseLogger {
    fun<T> loggingError(logName: String, response: Response<T>){
        try {
            val errorMessage = JSONObject(response.errorBody()?.string())

            Log.i(logName, "Code : ${response.code()}")
            Log.i(logName, "message : ${errorMessage.getString("message")}")
        } catch (e: java.lang.Exception){
            e.printStackTrace()
            Log.i(logName + "응답 없음", response.body().toString())
        }
    }
}