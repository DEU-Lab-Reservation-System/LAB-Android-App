package com.example.lab.utils.extension

import com.example.lab.R
import com.example.lab.data.enum.Role

/**
 * 라디오 버튼에 따라 회원 유형을 반환하는 메소드
 */
fun Role.userRole(checkedId: Int): Role {
    return when(checkedId){
        R.id.student -> Role.USER
        R.id.takeoff -> Role.USER_TAKEOFF
        R.id.graduate -> Role.USER_GRADUATE
        R.id.prof -> Role.PROF
        R.id.admin -> Role.ADMIN
        else -> Role.USER
    }
}