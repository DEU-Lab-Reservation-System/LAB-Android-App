package com.example.lab.view.viewinitializer.reserv
import android.app.AlertDialog

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab.R
import com.example.lab.databinding.FragmentReservBinding
import com.example.lab.view.fragment.ReservFragment
import com.example.lab.view.fragment.UserListInLabFragment
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
        bind.reservBtn.setOnClickListener{
            fragment.apply {
                val startTime = "${bind.startTimeEditText.editText?.text}"
                val endTime = "${bind.endTimeEditText.editText?.text}"
                val lab = bind.labSelector.selectedItem as String

                // 시간 선택이 다 안된 경우
                if(startTime.isEmpty() || endTime.isNullOrEmpty()){
                    val alertDialog: AlertDialog? = activity?.let {
                        val builder = AlertDialog.Builder(it)
                        builder.apply {
                            setTitle("시스템 알림")
                            setMessage("모든 입력 값을 선택해주세요.")
                            setPositiveButton("확인") { dialog, _ -> dialog.dismiss()}
                        }
                        builder.create()
                    }
                    alertDialog?.show()
                    return@setOnClickListener
                }

                val userListFragment = UserListInLabFragment()
                userListFragment.arguments = Bundle().apply {
                    putString("startTime", startTime)
                    putString("endTime", endTime)
                    putString("roomNum", lab)
                }

                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout, userListFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}