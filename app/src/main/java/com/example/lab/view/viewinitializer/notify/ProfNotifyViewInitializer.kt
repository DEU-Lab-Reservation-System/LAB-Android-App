package com.example.lab.view.viewinitializer.notify

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.lab.databinding.FragmentNotificationBinding
import com.example.lab.view.fragment.NotificationFragment
import com.example.lab.view.viewinitializer.ViewInitializer

class ProfNotifyViewInitializer: ViewInitializer {
    private lateinit var fragment: NotificationFragment
    private lateinit var bind: FragmentNotificationBinding

    override fun init(fragment: Fragment, bind: ViewDataBinding) {
        this.fragment = fragment as NotificationFragment
        this.bind = bind as FragmentNotificationBinding

        initView()
    }

    private fun initView(){
        bind.apply {
            notifyTitleTv.text = "알림 내역"
            notifyContentTv.text = ""
        }
    }
}