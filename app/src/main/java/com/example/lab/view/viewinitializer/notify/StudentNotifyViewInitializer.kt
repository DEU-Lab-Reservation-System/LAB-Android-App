package com.example.lab.view.viewinitializer.notify

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.lab.databinding.FragmentNotificationBinding
import com.example.lab.databinding.FragmentReservBinding
import com.example.lab.view.fragment.NotificationFragment
import com.example.lab.view.fragment.ReservFragment
import com.example.lab.view.viewinitializer.ViewInitializer
import com.example.lab.viewmodel.ReservViewModel

class StudentNotifyViewInitializer: ViewInitializer {
    private lateinit var fragment: NotificationFragment
    private lateinit var bind: FragmentNotificationBinding

    override fun init(fragment: Fragment, bind: ViewDataBinding) {
        this.fragment = fragment as NotificationFragment
        this.bind = bind as FragmentNotificationBinding

        initView()
    }

    private fun initView(){
        bind.apply {
            notifyTitleTv.text      = "알림 내역"
            notifyContentTv.text    = "방장 변경, 예약 승인에 대한 알림을 확인하세요."
            approvalBtn.visibility  = View.GONE
            rejectBtn.visibility    = View.GONE
        }
    }
}