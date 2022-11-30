package com.example.lab.view.viewinitializer.reserv

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lab.R
import com.example.lab.application.MyApplication
import com.example.lab.data.requestDto.ReservRequestDto
import com.example.lab.databinding.FragmentReservBinding
import com.example.lab.view.fragment.NoticeFragment
import com.example.lab.view.fragment.ReservFragment
import com.example.lab.view.viewinitializer.ViewInitializer
import com.example.lab.viewmodel.ReservViewModel

class StuReservViewInitializer : ViewInitializer {
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
        addEventreservationBtn()
    }

    /**
     * 학생 뷰에 맞게 뷰를 초기화 (숨겨놨던 것들 표시)
     */
    private fun initView(){
        bind.apply {
            // 팀 선택 레이아웃 숨기기
            teamLayout.visibility = View.VISIBLE
            reservBtn.text = "예약 신청"
        }
    }

    /**
     * 예약 신청 버튼 이벤트 등록
     */
    private fun addEventreservationBtn() {
        fragment.apply {
            bind.reservBtn.setOnClickListener(View.OnClickListener {
                bind.apply {
                    val reservInfo = ReservRequestDto.Create(
                        userId = MyApplication.member?.userId ?: "",
                        labNum = labSelector.selectedItem as String,
                        team = if (teamSelector.text.isEmpty()) -1 else teamSelector.text.toString()
                            .toInt(),
                        seatNum = selectedSeatTv.text.toString(),
                        startTime = startTimeEditText.editText?.text.toString(),
                        endTime = endTimeEditText.editText?.text.toString()
                    )

                    // 모든 선택 값이 다 선택된 경우에만 예약 신청 가능
                    reservInfo.apply {
                        // 예약 시작 시간이 종료 시간보다 늦는 경우
                        if(startTime >= endTime){
                            val alertDialog: AlertDialog? = activity?.let {
                                val builder = AlertDialog.Builder(it)
                                builder.apply {
                                    setTitle("시스템 알림")
                                    setMessage("예약 시작 시간은 종료 시간보다 늦을 수 없습니다.")
                                    setPositiveButton("확인") { dialog, _ -> dialog.dismiss()}
                                }.create()
                            }
                            alertDialog?.show()
                            return@OnClickListener
                        }

                        if (startTime.isEmpty() || endTime.isEmpty() ||
                            team == -1 || seatNum == "-"
                        ) {
                            val alertDialog: AlertDialog? = activity?.let {
                                val builder = AlertDialog.Builder(it)
                                builder.apply {
                                    setTitle("시스템 알림")
                                    setMessage("모든 항목을 선택해주세요.")
                                    setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
                                }
                                builder.create()
                            }
                            alertDialog?.show()
                            return@OnClickListener
                        }
                    }
                    // 예약 신청
                    reservVM.addReservation(reservInfo)
                }
            })

            // 예약 성공시
            reservVM.reserv.observe(viewLifecycleOwner) {
                val fragment = NoticeFragment().apply {
                    arguments = Bundle().apply {
                        putString("ReservInfo", it.toJson().toString())
                    }
                }

                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .add(R.id.frameLayout, fragment)
                    .addToBackStack(null)
                    .commit()
            }

            // 예약 실패시
            reservVM.reservError.observe(viewLifecycleOwner) { error ->
                val alertDialog: AlertDialog? = activity?.let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        setTitle("예약 실패")
                        setMessage(error.contentIfNotHandled() ?: "예약에 실패했습니다.")
                        setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
                    }
                    builder.create()
                }
                alertDialog?.show()
            }
        }
    }

}