package com.example.lab.view.viewinitializer.reserv

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab.databinding.FragmentReservBinding
import com.example.lab.view.fragment.ReservFragment
import com.example.lab.view.viewinitializer.ViewInitializer
import com.example.lab.viewmodel.ReservViewModel

class AdminReservViewInitializer: ViewInitializer {
    private lateinit var fragment: ReservFragment
    private lateinit var bind: FragmentReservBinding
    private lateinit var reservVM: ReservViewModel

    override fun init(fragment: Fragment, bind: ViewDataBinding) {
        this.fragment = fragment as ReservFragment
        this.bind = bind as FragmentReservBinding

        this.fragment.apply {
            reservVM = ViewModelProvider(requireActivity())[ReservViewModel::class.java]
        }

        initView()
        addClickEventReservBtn()
    }

    private fun initView(){
        bind.apply {
            teamLayout.visibility = View.GONE
            reservBtn.text = "이용자 목록 조회"
        }
    }

    /**
     * 예약(조교는 이용자 조회) 버튼 클릭 이벤트
     */
    private fun addClickEventReservBtn(){
        bind.reservBtn.setOnClickListener(null)
    }
}