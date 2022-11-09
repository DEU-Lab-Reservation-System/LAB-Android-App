package com.example.lab.repository

import com.example.lab.data.requestDto.TokenRequestDto
import com.example.lab.remote.RetrofitClient
import com.example.lab.remote.service.TokenService
import com.example.lab.data.responseDto.TokenResponseDto
import retrofit2.Call
import retrofit2.Response
import java.io.IOException


object TokenRepository {
    private val tokenService: TokenService = RetrofitClient.retrofit.create(TokenService::class.java)

    fun checkToken(userId:String, inputToken:String):Response<TokenResponseDto.Check>? {
        val tokenCall: Call<TokenResponseDto.Check> = tokenService.checkToken(TokenRequestDto.Check(userId, inputToken))

        return try{
            tokenCall.execute()
        }catch (e: IOException){
            return null
        }

    }
}