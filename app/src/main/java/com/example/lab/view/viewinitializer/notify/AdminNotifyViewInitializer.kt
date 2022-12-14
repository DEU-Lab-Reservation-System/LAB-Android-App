package com.example.lab.view.viewinitializer.notify

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.lab.databinding.FragmentNotificationBinding
import com.example.lab.view.fragment.NotificationFragment
import com.example.lab.view.viewinitializer.ViewInitializer

class AdminNotifyViewInitializer: ViewInitializer {
    private lateinit var fragment: NotificationFragment
    private lateinit var bind: FragmentNotificationBinding

    override fun init(fragment: Fragment, bind: ViewDataBinding) {
        this.fragment = fragment as NotificationFragment
        this.bind = bind as FragmentNotificationBinding

        initView()
    }
    
    private fun initView(){
        bind.apply { 
            notifyTitleTv.text      = "예약 신청 내역"
            notifyContentTv.text    = "승인하지 않은 내역들은 1일이 지나면 모두 사라집니다."
            approvalBtn.visibility  = View.VISIBLE
            rejectBtn.visibility    = View.VISIBLE
        }
    }
}