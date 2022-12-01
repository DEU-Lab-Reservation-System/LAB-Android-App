package com.example.lab.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab.application.MyApplication
import com.example.lab.data.requestDto.MemberRequestDto
import com.example.lab.data.responseDto.MemberResponseDto
import com.example.lab.data.responseDto.MessageDto
import com.example.lab.remote.repository.MemberRepository
import com.example.lab.utils.Event
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(DelicateCoroutinesApi::class)
class MemberViewModel: ViewModel() {
    val loginFlag = MutableLiveData<Boolean>(false)
    val signUpFlag = MutableLiveData<Boolean>(false)
    val idCheckFlag = MutableLiveData<Boolean>(false)
    val updateFlag = MutableLiveData<Event<Boolean>>()
    val memberList = MutableLiveData<MemberResponseDto.Members>()
    val withdrawResult = MutableLiveData<MessageDto>()
    val updateUser = MutableLiveData<MemberResponseDto.Member>()
    val warningResult = MutableLiveData<MessageDto>()
    val resetResult = MutableLiveData<MessageDto>()

    val loginError = MutableLiveData<Event<String>>()
    val signUpError = MutableLiveData<Event<String>>()
    val idCheckError = MutableLiveData<Event<String>>()
    var updateError:String?=null
    val membersError = MutableLiveData<String>()
    val withdrawError = MutableLiveData<String>()
    val warningError = MutableLiveData<String>()
    val resetError = MutableLiveData<String>()

    /**
     * 로그인 메소드
     */
    fun login(id: String, password: String){
        GlobalScope.launch(Dispatchers.IO) {
            val response = MemberRepository.login(id, password, MyApplication.deviceToken!!)

            response?.let {
                if(it.isSuccessful){
                    val member = it.body()

                    // 전역 변수로 저장
                    MyApplication.member = member

                    loginFlag.postValue(true)
                    Log.i("로그인 성공", it.body().toString())
                }else {
                    val errorMessage = it.errorBody()?.string()?.let { res -> JSONObject(res) }

                    loginError.postValue(Event(errorMessage?.getString("message")?:""))
                    Log.i("로그인 실패", "${it.code()}, ${errorMessage?.getString("message")?:""} ")
                }
            }
        }
    }

    /**
     * 회원가입 메소드
     */
    fun signUp(signUpDto: MemberRequestDto.SignUp){
        GlobalScope.launch(Dispatchers.IO){
            val response = MemberRepository.signUp(signUpDto)

            response?.let {
                if(it.isSuccessful){
                    signUpFlag.postValue(true)
                    Log.i("회원 가입 완료", it.body().toString())
                } else {
                    val errorMessage = it.errorBody()?.string()?.let { res -> JSONObject(res) }

                    signUpError.postValue(Event(errorMessage?.getString("message")?:""))
                    
                    Log.i("회원 가입 실패", "${it.code()}, ${errorMessage?.getString("message")?:""}")
                }
            }
        }
    }

    /**
     * 아이디 중복 확인 메소드
     */
    fun idCheck(id:String){
        GlobalScope.launch(Dispatchers.IO) {
            val response = MemberRepository.idCheck(id)

            response?.let {
                if(it.isSuccessful){
                    idCheckFlag.postValue(true)
                    Log.i("아이디 중복 확인 성공", it.body().toString())
                } else {
                    val errorMessage = JSONObject(it.errorBody()?.string()!!)
                    idCheckError.postValue(Event(errorMessage.getString("message")?:""))

                    Log.i("아이디 중복 확인 실패", "${it.code()}, ${errorMessage.getString("message") ?:""}")
                }
            }
        }
    }

    /**
     * 회원 정보 수정 메소드
     */
    fun updateMember(member:MemberRequestDto.Update){
        GlobalScope.launch(Dispatchers.IO){
            val response = MemberRepository.updateMember(member)

            response?.let {
                if(it.isSuccessful){
                    it.body()?.let { body -> MyApplication.member?.update(body) }
                    updateFlag.postValue(Event(true))

                    Log.i("회원 정보 수정 성공", it.body().toString())
                } else {
                    updateFlag.postValue(Event(false))
                    val errorMessage = it.errorBody()?.string()?.let { res -> JSONObject(res) }
                    updateError = errorMessage?.getString("message")?:"오류가 발생했습니다."

                    Log.i("회원 정보 수정 실패", "${it.code()}, $updateError")
                }
            }
        }
    }

    /**
     * 회원 정보 수정(조교) 메소드
     */
    fun updateMemberOfAdmin(member:MemberRequestDto.Update){
        GlobalScope.launch(Dispatchers.IO){
            val response = MemberRepository.updateMember(member)

            response?.let {
                if(it.isSuccessful){
                    updateUser.postValue(it.body())
                    Log.i("회원 정보 수정 성공", it.body().toString())
                } else {
                    updateFlag.postValue(Event(false))
                    val errorMessage = it.errorBody()?.string()?.let { res -> JSONObject(res) }
                    updateError = errorMessage?.getString("message")?:"오류가 발생했습니다."

                    Log.i("회원 정보 수정 실패", "${it.code()}, $updateError")
                }
            }
        }
    }

    /**
     * 전체 사용자 정보 조회 메소드(USER만)
     */
    fun getAllMembers(){
        GlobalScope.launch(Dispatchers.IO){
            val response = MemberRepository.getAllMembers()

            response?.let {
                if(it.isSuccessful){
                    memberList.postValue(it.body())

                    Log.i("전체 회원 정보 조회 성공", it.body().toString())
                } else {
                    val errorMessage = it.errorBody()?.string()?.let { res -> JSONObject(res) }
                    membersError.postValue(errorMessage?.getString("message")?:"")

                    Log.i("회원 정보 수정 실패", "${it.code()}, ${errorMessage?.getString("message")?:""}")
                }
            }
        }
    }

    /**
     * 회원 탈퇴 메소드
     */
    fun withdrawal(userId:String){
        GlobalScope.launch(Dispatchers.IO){
            val response = MemberRepository.withdrawal(userId)

            response?.let {
                if(it.isSuccessful){
                    withdrawResult.postValue(it.body())
                    Log.i("회원 탈퇴 완료", it.body().toString())
                } else {
                    val errorMessage = it.errorBody()?.string()?.let { res -> JSONObject(res) }
                    withdrawError.postValue(errorMessage?.getString("message")?:"")

                    Log.i("회원 탈퇴 실패", "${it.code()}, ${errorMessage?.getString("message")?:""}")
                }
            }
        }
    }

    /**
     * 회원 경고
     */
    fun warning(userId:String){
        GlobalScope.launch(Dispatchers.IO){
            val response = MemberRepository.warning(userId)

            response?.let {
                if(it.isSuccessful){
                    warningResult.postValue(it.body())
                } else {
                    val errorMessage = it.errorBody()?.string()?.let { res -> JSONObject(res) }
                    warningError.postValue(errorMessage?.getString("message")?:"")
                }
            }
        }
    }

    /**
     * 회원 경고 초기화
     */
    fun resetWarning(userId:String){
        GlobalScope.launch(Dispatchers.IO){
            val response = MemberRepository.resetWarning(userId)

            response?.let {
                if(it.isSuccessful){
                    resetResult.postValue(it.body())
                } else {
                    val errorMessage = it.errorBody()?.string()?.let { res -> JSONObject(res) }
                    resetError.postValue(errorMessage?.getString("message")?:"")
                }
            }
        }
    }
}