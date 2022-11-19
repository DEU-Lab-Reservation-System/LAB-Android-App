package com.example.lab.view.viewinitializer.timetable

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.lab.databinding.FragmentTimeTableBinding
import com.example.lab.view.fragment.TimeTableFragment
import com.example.lab.view.viewinitializer.ViewInitializer
import java.util.*

class AdminTimeTableViewInitializer: ViewInitializer {
    private lateinit var fragment: TimeTableFragment
    private lateinit var bind: FragmentTimeTableBinding

    override fun init(fragment: Fragment, bind: ViewDataBinding) {
        this.fragment = fragment as TimeTableFragment
        this.bind = bind as FragmentTimeTableBinding

        initView()
    }

    private fun initView() {
        bind.apply {
            // 숨겨질 뷰

            // 보여질 뷰
            addClassBtn.visibility = View.VISIBLE // 수업 추가 버튼
            addClassBtn.text = "수업 추가"
        }
    }
}