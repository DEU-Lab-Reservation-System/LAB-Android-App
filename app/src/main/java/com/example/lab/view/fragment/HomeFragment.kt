package com.example.lab.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab.R
import com.example.lab.adapter.SeatAdapter
import com.example.lab.application.MyApplication
import com.example.lab.data.entity.Reservation
import com.example.lab.databinding.FragmentHomeBinding
import com.example.lab.databinding.SubSeatGridviewBinding
import com.example.lab.utils.DateManager
import com.example.lab.utils.DensityManager
import com.example.lab.viewmodel.LabViewModel
import com.example.lab.viewmodel.ReservViewModel
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@RequiresApi(Build.VERSION_CODES.O)
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var prevSelectSeat:View

    // VARIABLE
    private lateinit var bind: FragmentHomeBinding
    private lateinit var lablist:Array<String>
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    @SuppressLint("SimpleDateFormat")
    private val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH : mm")
    private lateinit var labVM:LabViewModel
    private lateinit var callback:OnBackPressedCallback // 뒤로가기 버튼 이벤트 캐치를 위한 콜백

    private lateinit var leftGridView  : GridView
    private lateinit var rightGridView : GridView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        // 그리드뷰 변수 연결
        leftGridView = bind.seatGridView.leftSeatGridView
        rightGridView = bind.seatGridView.rightSeatGridView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        // 데이터 바인딩
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        labVM = ViewModelProvider(requireActivity())[LabViewModel::class.java]

        /** 데이터를 관리하는 뷰 모델을 bind에 연결해줘야 적용 됨 */
        bind.lifecycleOwner = requireActivity()
        // bind.seatGridView.blurFrameLayout.visibility = View.VISIBLE

        // 그리드뷰 변수 연결
        leftGridView = bind.seatGridView.leftSeatGridView
        rightGridView = bind.seatGridView.rightSeatGridView

        initView()
        initGridView()
        initLabSpinner()
        setTodayReservation()

        return bind.root
    }


    /** 당일 예약 내역 배너 데이터 세팅 */
    @SuppressLint("SetTextI18n")
    private fun setTodayReservation(){
        val reserv:Reservation? = MyApplication.member?.reservation

        if(reserv == null){
            bind.todayReservPlaceTv.text = "예약 내역이 존재하지 않습니다."
            bind.todayReservTimeTv.text = "00:00 - 00:00"
            bind.todayReservSeatTv.text = "선택한 좌석이 없습니다."
            return
        }

        val startTime = reserv.startTime?.let { DateManager.dateParse(it) }
        val endTime = reserv.endTime?.let { DateManager.dateParse(it) }

        bind.todayReservPlaceTv.text = "정보공학관 ${reserv.roomNumber}호"
        bind.todayReservTimeTv.text = "$startTime - $endTime"
        bind.todayReservSeatTv.text = "${reserv.seatNum}번 좌석"
    }

    private fun initView(){
        bind.todayTV.text = dateFormat.format(Calendar.getInstance().timeInMillis)
    }

    /** 실습실 선택 스피너 초기화 */
    @SuppressLint("UseCompatLoadingForDrawables")
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
                labVM.getLabStatus(lablist[position].toInt())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        /**
         * 이용중인 좌석 표시
         */
        labVM.labStatus.observe(requireActivity()){
            /**
             * 0부터 실습실 좌석 수까지 순회 (그리드뷰 반반씩 나눠져 있으니 / 2 )
             * 인덱스에 해당하는 gridView의 item(실제 좌석 번호)을 가져옴
             * item이 seatlist에 있으면 해당 아이템의 뷰의 색상을 변경
             * 없으면 회색으로 돌려놓기
             */
            val seatlist:ArrayList<Int> = (labVM.labStatus.value?.seatList?:arrayListOf()).map { it.toInt() } as ArrayList<Int>

            fun markSeatInUse(gridView: GridView, idx:Int){
                val seatNum = gridView.getItemAtPosition(idx) as Int

                if(seatlist.contains(seatNum)){
                    gridView[idx].findViewById<View>(R.id.seat).background = resources.getDrawable(R.drawable.shape_seat_selected)
                } else {
                    gridView[idx].findViewById<View>(R.id.seat).background = resources.getDrawable(R.drawable.shape_seat)
                }
            }

            for (i in 0 until LAB_SEAT_SIZE / 2) {
                markSeatInUse(leftGridView, i)
                markSeatInUse(rightGridView, i)
            }
        }
    }

    private fun initGridView(){
        // 그리드뷰를 include로 불러왔으므로 include한 레이아웃을 먼저 가져옴
        val seatGridView: SubSeatGridviewBinding = bind.seatGridView

        bind.todayTimeTV.text = dateTimeFormat.format(Calendar.getInstance().timeInMillis)

        val leftSeatList: MutableList<Int> = mutableListOf()
        val rightSeatList: MutableList<Int> = mutableListOf()
        
        // 좌석 번호 세팅
        var flag = true
        for (i in 1..32){
            if(flag) leftSeatList.add(i)
            else rightSeatList.add(i)

            if(i % 4 == 0) flag = !flag
        }

        // 어댑터 생성 및 연결
        leftGridView.adapter = SeatAdapter(context = requireContext(), leftSeatList)
        rightGridView.adapter = SeatAdapter(context = requireContext(), rightSeatList)

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


    /** 뒤로가기 버튼 클릭 이벤트 */
    private val limitTime = 1000        // 뒤로가기 버튼 누르는 제한시간
    private var pressTime:Long = 0L     // 누른 시점

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val now = System.currentTimeMillis()
                val interval = now - pressTime

                if (interval in 0..limitTime) {
                    requireActivity().finish()
                } else {
                    pressTime = now
                    Toast.makeText(requireContext(), "한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        private const val LAB_SEAT_SIZE = 32 // 실습실 좌석 총 개수
        
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}