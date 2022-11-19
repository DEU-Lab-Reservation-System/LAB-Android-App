package com.example.lab.view.viewinitializer.timetable

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.lab.databinding.FragmentTimeTableBinding
import com.example.lab.view.fragment.TimeTableFragment
import com.example.lab.view.viewinitializer.ViewInitializer

class StudentTimeTableViewInitializer: ViewInitializer {
    private lateinit var fragment: TimeTableFragment
    private lateinit var bind: FragmentTimeTableBinding

    override fun init(fragment: Fragment, bind: ViewDataBinding) {
        this.fragment = fragment as TimeTableFragment
        this.bind = bind as FragmentTimeTableBinding

        initView()
    }

    private fun initView(){
        bind.apply {
            // 숨겨질 뷰
            addClassBtn.visibility  = View.GONE // 수업 추가 버튼
        }
    }
}