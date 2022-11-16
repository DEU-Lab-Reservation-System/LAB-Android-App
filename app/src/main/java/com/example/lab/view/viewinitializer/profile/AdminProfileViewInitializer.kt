package com.example.lab.view.viewinitializer.profile

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.lab.databinding.FragmentNotificationBinding
import com.example.lab.databinding.FragmentProfileBinding
import com.example.lab.view.fragment.NotificationFragment
import com.example.lab.view.fragment.ProfileFragment
import com.example.lab.view.viewinitializer.ViewInitializer

class AdminProfileViewInitializer:ViewInitializer {
    private lateinit var fragment: ProfileFragment
    private lateinit var bind: FragmentProfileBinding
    override fun init(fragment: Fragment, bind: ViewDataBinding) {
        this.fragment = fragment as ProfileFragment
        this.bind = bind as FragmentProfileBinding

        initView()
    }

    private fun initView(){
        bind.apply {
            // 숨겨지는 레이아웃
            reservHistoryMenuLayout.visibility  = View.GONE
            reportMenuLayout.visibility         = View.GONE

            // 보여지는 레이아웃
            userListMenuLayout.visibility       = View.VISIBLE   // 회원 목록 조회
            reportListMenuLayout.visibility     = View.VISIBLE   // 불편사항 접수
        }
    }
}