package com.example.lab.remote.repository

import com.example.lab.data.entity.Member
import com.example.lab.remote.RetrofitClient
import com.example.lab.remote.service.MemberService
import com.example.lab.data.requestDto.MemberRequestDto
import com.example.lab.data.responseDto.MemberResponseDto
import com.example.lab.data.responseDto.MessageDto
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import kotlin.math.sign

object MemberRepository {
    private val memberService: MemberService = RetrofitClient.retrofit.create(MemberService::class.java)

    /**
     *  로그인 요청 메소드
     */
    fun login(id: String, password: String, deviceToken:String): Response<Member>?{
        val memberCall: Call<Member>? = memberService.login(MemberRequestDto.Login(id, password, deviceToken))

        return try {
            memberCall?.execute()
        } catch (e: IOException){
            e.printStackTrace()
            null
        }
    }

    /**
     * 회원가입 메소드
     */
    fun signUp(signUpDto: MemberRequestDto.SignUp): Response<MessageDto>?{
        val memberCall: Call<MessageDto> = memberService.signUp(signUpDto)

        return try{
            memberCall.execute()
        } catch (e: IOException){
            e.printStackTrace()
            null
        }
    }

    /**
     * 아이디 중복 확인 메소드
     */
    fun idCheck(id:String): Response<MessageDto>? {
        val memberCall: Call<MessageDto> = memberService.idCheck(MemberRequestDto.Check(id))

        return try{
            memberCall.execute()
        } catch (e: IOException){
            e.printStackTrace()
            null
        }
    }

    /**
     * 회원 정보 수정 메소드
     */
    fun updateMember(member:MemberRequestDto.Update): Response<MemberResponseDto.Member>? {
        val memberCall: Call<MemberResponseDto.Member> = memberService.updateMember(member)

        return try{
            memberCall.execute()
        } catch (e: IOException){
            e.printStackTrace()
            null
        }
    }
}