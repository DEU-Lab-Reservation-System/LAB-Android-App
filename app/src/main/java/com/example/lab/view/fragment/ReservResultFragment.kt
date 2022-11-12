package com.example.lab.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab.R
import com.example.lab.adapter.SeatAdapter
import com.example.lab.application.MyApplication
import com.example.lab.databinding.FragmentReservResultBinding
import com.example.lab.databinding.SubSeatGridviewBinding
import com.example.lab.utils.DateManager
import com.example.lab.utils.DensityManager
import com.example.lab.viewmodel.ReservViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReservResult.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReservResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // VARIABLE
    private lateinit var bind: FragmentReservResultBinding
    private lateinit var reservVM: ReservViewModel
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_reserv_result, container, false)
        reservVM = ViewModelProvider(requireActivity())[ReservViewModel::class.java]

        /** 데이터를 관리하는 뷰 모델을 bind에 연결해줘야 적용 됨 */
        bind.lifecycleOwner = requireActivity()

        activity?.let {
            it.findViewById<BottomNavigationView>(R.id.bottomNavbar).visibility = View.GONE
        }

        /** 데이터를 관리하는 뷰 모델을 bind에 연결해줘야 적용 됨 */
        initGridView()
        addButtonEvent()
        initReservData()

        return bind.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun initReservData(){
        reservVM.getUserReservs(MyApplication.member?.userId!!)

        reservVM.reservList.observe(requireActivity()){

            if(it.reservList.isNotEmpty()){
                it.reservList[0].let {
                    bind.apply {
                        studentNameTv.text = "${it.name}(${it.userId})"
                        majorTv.text = it.major
                        replaceTv.text = "정보공학관 ${it.roomNumber}"
                        timeTv.text = "${DateManager.dateParse(it.startTime)}-${DateManager.dateParse(it.endTime)}"
                        seatTv.text = "${it.seatNum}번 좌석"
                    }
                }
            }
        }
    }

    private fun addButtonEvent(){
        bind.okBtn.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun initGridView(){
        // 그리드뷰를 include로 불러왔으므로 include한 레이아웃을 먼저 가져옴
        val seatGridView: SubSeatGridviewBinding = bind.seatGridView

        bind.todayTimeTV.text = dateFormat.format(Calendar.getInstance().timeInMillis)

        val leftSeatList: MutableList<Int> = mutableListOf()
        val rightSeatList: MutableList<Int> = mutableListOf()

        // 좌석 번호 세팅
        var flag = true
        for (i in 1..32){
            if(flag) leftSeatList.add(i)
            else rightSeatList.add(i)

            if(i % 4 == 0) flag = !flag
        }

        // 어댑터 생성
        seatGridView.leftSeatGridView.adapter = SeatAdapter(context = requireContext(), leftSeatList)
        seatGridView.rightSeatGridView.adapter = SeatAdapter(context = requireContext(), rightSeatList)

        /** BlurView 높이 동적으로 변경 */
        seatGridView.labSeatLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                val params = seatGridView.blurView.layoutParams

                seatGridView.blurView.layoutParams = params.apply {
                    height = seatGridView.labSeatLayout.height + DensityManager.convertDPtoPX(30)
                }

                seatGridView.labSeatLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.beginTransaction().remove(this@ReservResultFragment).commit();
                requireActivity().supportFragmentManager.popBackStack();
            }
        })
    }

    override fun onPause() {
        super.onPause()

        activity?.let {
            it.findViewById<BottomNavigationView>(R.id.bottomNavbar).visibility = View.VISIBLE
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReservResult.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReservResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}