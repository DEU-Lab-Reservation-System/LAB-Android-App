package com.example.lab.data.remote.service

import retrofit2.Call
import com.example.lab.data.entity.Member
import retrofit2.http.POST

interface UserService {
    @POST
    fun login(id:String, password:String): Call<Member>?
}