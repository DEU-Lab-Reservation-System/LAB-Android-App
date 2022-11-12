package com.example.lab.view.viewinitializer.reserv

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.lab.databinding.FragmentReservBinding
import com.example.lab.view.fragment.ReservFragment
import com.example.lab.view.viewinitializer.ViewInitializer

class ProfReservViewInitializer: ViewInitializer {
    private lateinit var fragment: ReservFragment
    private lateinit var bind: FragmentReservBinding

    override fun init(fragment: Fragment, bind: ViewDataBinding) {
        this.fragment = fragment as ReservFragment
        this.bind = bind as FragmentReservBinding

        initView()
    }

    private fun initView(){
        bind.apply {
            teamLayout.visibility = View.GONE
            reservBtn.visibility = View.GONE
        }
    }
}