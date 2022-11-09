package com.example.lab.remote.service

import com.example.lab.data.requestDto.TokenRequestDto
import com.example.lab.data.responseDto.TokenResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenService {

    @POST("api/token")
    fun createToken(): Call<String>

    @POST("api/token/check")
    fun checkToken(@Body tokenCheckDto: TokenRequestDto.Check): Call<TokenResponseDto.Check>

}