package com.example.lab.view.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import androidx.core.view.get
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab.R
import com.example.lab.adapter.SeatAdapter
import com.example.lab.adapter.data.SeatStatus
import com.example.lab.application.MyApplication
import com.example.lab.data.requestDto.LabRequestDto
import com.example.lab.data.requestDto.ReservRequestDto
import com.example.lab.databinding.FragmentReservBinding
import com.example.lab.databinding.SubSeatGridviewBinding
import com.example.lab.utils.DensityManager
import com.example.lab.view.viewinitializer.ViewInitializerFactory
import com.example.lab.viewmodel.LabViewModel
import com.example.lab.viewmodel.ReservViewModel
import com.google.android.datatransport.runtime.backends.BackendResponse.ok
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReservFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReservFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var bind:FragmentReservBinding
    private lateinit var lablist:Array<String>

    private lateinit var prevSelectSeat:View
    private lateinit var leftGridView  : GridView
    private lateinit var rightGridView : GridView

    private lateinit var labVM:LabViewModel
    private lateinit var reservVM:ReservViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        initTeamSpinner()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // 데이터 바인딩
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_reserv, container, false)
        labVM = ViewModelProvider(requireActivity())[LabViewModel::class.java]
        reservVM = ViewModelProvider(requireActivity())[ReservViewModel::class.java]

        MyApplication.member?.let {
            ViewInitializerFactory().getInitializer(it.role, "RESERVATION").init(this, bind)
        }

        /** 데이터를 관리하는 뷰 모델을 bind에 연결해줘야 적용 됨 */
        bind.lifecycleOwner = requireActivity()

        // 그리드뷰 변수 연결
        leftGridView = bind.seatGridView.leftSeatGridView
        rightGridView = bind.seatGridView.rightSeatGridView

        initView()
        initGridView()
        initTeamSpinner()
        initLabSpinner()
//        addEventreservationBtn()

        setLabStatus()          // 실습실 상태 표시

        return bind.root
    }

    private fun initView(){
        // EditText에 TimePicker 등록
        bind.apply {
            startTimeEditText.editText?.addTimePicker()
            endTimeEditText.editText?.addTimePicker()
        }
    }

    /**
     * 팀 선택 스피너 초기화
     */
    private fun initTeamSpinner(){
        // ArrayAdapter.createFromResource가 ArrayAdapter를 반환하므로 also로 adapter를 초기화한 후 할당
        bind.teamSelector.setAdapter(
                ArrayAdapter.createFromResource(
                requireContext(),
                R.array.team_list,
                R.layout.spinner_custom_item_padding
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.select_dialog_item)
            })
    }

    /**
     * 실습실 선택 스피너 초기화
     * 선택된 시간이 없으면 현재 시간 기준으로 조회
     * 선택된 시간이 있으면 해당 시간대로 조회
     */
    private fun initLabSpinner(){
        // 스피너는 고정된 리스트를 보여주는 것이기 때문에 xml로 따로 관리하는 것이 좋음
        lablist = resources.getStringArray(R.array.lab_list)
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, lablist)

        // 어댑터 등록
        bind.labSelector.adapter = spinnerAdapter
        bind.labSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                bind.labNumber.text = "${lablist[position]}호 좌석 현황"

                val timeRange = bind.run {
                    val startTime = startTimeEditText.editText?.text.toString()
                    val endTime = endTimeEditText.editText?.text.toString()

                    if(startTime.isBlank() || endTime.isBlank()) null
                    else LabRequestDto.TimeRange(startTime, endTime)
                }

                labVM.getLabStatus(lablist[position].toInt(), timeRange) // 선택 된 실슬실의 상태를 조회
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    /**
     * 실습실의 이용중인 좌석 표시 or 수업 중인지 표시
     */
    @SuppressLint("SetTextI18n")
    private fun setLabStatus(){
        // bind.seatGridView.blurFrameLayout.visibility = View.VISIBLE
        labVM.labStatus.observe(viewLifecycleOwner){
            /**
             * 0부터 실습실 좌석 수까지 순회 (그리드뷰 반반씩 나눠져 있으니 / 2 )
             * 인덱스에 해당하는 gridView의 item(실제 좌석 번호)을 가져옴
             * item이 seatlist에 있으면 해당 아이템의 뷰의 색상을 변경
             * 없으면 회색으로 돌려놓기
             */
            // 수업 중이면 수업 중임을 표시
            if(it.inClass){
                bind.seatGridView.apply {
                    blurFrameLayout.visibility = View.VISIBLE
                    leftGridView.isEnabled = false
                    rightGridView.isEnabled = false
                }
                return@observe
            }

            val seatlist:ArrayList<Int> = (it.seatList?:arrayListOf()).map {seat -> seat.toInt() } as ArrayList<Int>
            bind.seatGridView.apply {
                blurFrameLayout.visibility = View.GONE
                leftGridView.isEnabled = true
                rightGridView.isEnabled = true

                peopleTv.text = "${seatlist.size} / 32"
                managerTv.text =
                    if(it.manager == null) "방장이 없습니다."
                    else "${it.manager.name}(${it.manager.id})"
            }

            for (i in 0 until LAB_SEAT_SIZE / 2) {
                leftGridView.markSeatInUser(seatlist, i)
                rightGridView.markSeatInUser(seatlist, i)
            }
        }
    }


    /**
     * 그리드뷰 초기화 메소드
     */
    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    private fun initGridView(){
        // 그리드뷰를 include로 불러왔으므로 include한 레이아웃을 먼저 가져옴
        val seatGridView: SubSeatGridviewBinding = bind.seatGridView

        val leftSeatList: ArrayList<SeatStatus> = arrayListOf()
        val rightSeatList: ArrayList<SeatStatus> = arrayListOf()

        // 좌석 번호 세팅
        var flag = true
        for (i in 1..32){
            if(flag) leftSeatList.add(SeatStatus(i))
            else rightSeatList.add(SeatStatus(i))

            if(i % 4 == 0) flag = !flag
        }

        // 어댑터 생성 및 연결
        leftGridView.adapter = SeatAdapter(context = requireContext(), leftSeatList)
        rightGridView.adapter = SeatAdapter(context = requireContext(), rightSeatList)

        // 왼쪽, 오른쪽 그리드뷰에 각각 이벤트 등록
        seatGridView.apply {
            leftSeatGridView.addSeatClickEvent(leftSeatList)
            rightSeatGridView.addSeatClickEvent(rightSeatList)
        }

        /** BlurView 높이 동적으로 변경 */
        seatGridView.labSeatLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                val params = seatGridView.blurView.layoutParams

                seatGridView.blurView.layoutParams = params.apply {
                    height = seatGridView.labSeatLayout.height
                }

                seatGridView.labSeatLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }


    /**
     * 그리드뷰 확장 함수
     * 사용 중인 좌석을 표시하는 메소드
     */
    private fun GridView.markSeatInUser(seatlist:ArrayList<Int>, idx:Int){
        val seatNum = this.getItemAtPosition(idx) as SeatStatus
        // 그리드뷰가 초기화 되기 전에 옵저버가 호출될 수 있으므로 Empty 체크
        if(this.isNotEmpty()){
            this[idx].findViewById<View>(R.id.seat).apply {
                background = if(seatlist.contains(seatNum.idx)) resources.getDrawable(R.drawable.shape_seat_selected)
                else resources.getDrawable(R.drawable.shape_seat)
            }
        }
    }

    /**
     * 그리드뷰 확장 함수
     * 그리드뷰에 클릭 이벤트를 등록하는 메소드
     */
    private fun GridView.addSeatClickEvent(seatList:ArrayList<SeatStatus>){
        this.onItemClickListener = AdapterView.OnItemClickListener{ adapterView, view, position, l ->
            val seat = view.findViewById(R.id.seat) as View // 클릭한 좌석의 뷰

            // 이전에 선택했던 자리는 다시 회색으로 돌림
            if (::prevSelectSeat.isInitialized) {
                prevSelectSeat.background = resources.getDrawable(R.drawable.shape_seat)
            }

            prevSelectSeat = seat
            seat.background = resources.getDrawable(R.drawable.shape_seat_selected)

            bind.selectedSeatTv.text = "${seatList[position].idx}"
        }
    }

    /**
     * Edittext 확장 함수
     * EditText에 TimePicker 등록
     */
    private fun EditText.addTimePicker(){
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR)

        this.setOnClickListener{
            val timePicker = TimePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, { timePicker, selectHour, selectMinute ->
                this.setText(String.format("%02d:%02d", selectHour, selectMinute))
            }, hour, 0, true)

            timePicker.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            timePicker.show()
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReservFragment.
         */
        // TODO: Rename and change types and number of parameters
        private const val LAB_SEAT_SIZE = 32 // 실습실 좌석 총 개수

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReservFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}