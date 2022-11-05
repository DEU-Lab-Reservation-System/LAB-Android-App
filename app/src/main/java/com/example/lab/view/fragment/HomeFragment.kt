package com.example.lab.view.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.lab.R
import com.example.lab.adapter.SeatAdapter
import com.example.lab.application.MyApplication
import com.example.lab.data.entity.Reservation
import com.example.lab.databinding.FragmentHomeBinding
import com.example.lab.databinding.SubSeatGridviewBinding
import com.example.lab.utils.DateManager
import com.example.lab.utils.DensityManager
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    private lateinit var labSeatLayout:LinearLayout
    private lateinit var lablist:Array<String>
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    private val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH : mm")
    private lateinit var callback:OnBackPressedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        // 데이터 바인딩
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        /** 데이터를 관리하는 뷰 모델을 bind에 연결해줘야 적용 됨 */
        bind.lifecycleOwner = requireActivity()
//        bind.seatGridView.blurFrameLayout.visibility = View.VISIBLE

        initView()
        initGridView()
        initLabSpinner()
        setTodayReservation()

        return bind.root
    }


    /** 당일 예약 내역 배너 데이터 세팅 */
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
    private fun initLabSpinner(){
        // 스피너는 고정된 리스트를 보여주는 것이기 때문에 xml로 따로 관리하는 것이 좋음
        lablist = resources.getStringArray(R.array.lab_list)

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, lablist)

        // 어댑터 등록
        bind.labSelector.adapter = spinnerAdapter
        bind.labSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                bind.labNumber.text = "${lablist[position]}호 좌석 현황"
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun initGridView(){
        // 그리드뷰를 include로 불러왔으므로 include한 레이아웃을 먼저 가져옴
        var seatGridView: SubSeatGridviewBinding = bind.seatGridView

        bind.todayTimeTV.text = dateTimeFormat.format(Calendar.getInstance().timeInMillis)

        var leftSeatList: MutableList<Int> = mutableListOf()
        var rightSeatList: MutableList<Int> = mutableListOf()
        
        // 좌석 번호 세팅
        var flag = true
        for (i in 1..32){
            if(flag) leftSeatList.add(i)
            else rightSeatList.add(i)

            if(i % 4 == 0) flag = !flag
        }

        // 어댑터 생성
        var leftSeatAdapter:SeatAdapter = SeatAdapter(context = requireContext(), leftSeatList)
        var rightSeatAdapter:SeatAdapter = SeatAdapter(context = requireContext(), rightSeatList)

        seatGridView.leftSeatGridView.adapter = leftSeatAdapter
        seatGridView.rightSeatGridView.adapter = rightSeatAdapter

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

        //        addSeatGridViewOnClickListener(seatGridView.leftSeatGridView, leftSeatList)
//        addSeatGridViewOnClickListener(seatGridView.rightSeatGridView, rightSeatList)
    }

//    private fun addSeatGridViewOnClickListener(gridView: GridView, seatList:MutableList<Int>){
//        // 클릭 이벤트 : 클릭한 좌석 표시
//        gridView.onItemClickListener = OnItemClickListener { adapterView, view, position, l ->
//            // 이전에 선택했던 자리는 다시 회색으로 돌림
//            if(::prevSelectSeat.isInitialized){
//                prevSelectSeat.background = resources.getDrawable(R.drawable.shape_seat)
//            }
//            // view는 현재 클릭 된 뷰
//            var seat = view.findViewById(R.id.seat) as View
//            prevSelectSeat = seat
//
//            // 선택 된 좌석은 색깔로 표시
//            seat.background = resources.getDrawable(R.drawable.shape_seat_selected)
//
//            Toast.makeText(requireContext(), "${seatList[position]} 번 좌석", Toast.LENGTH_SHORT).show()
//        }
//    }



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