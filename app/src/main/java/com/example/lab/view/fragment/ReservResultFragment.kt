package com.example.lab.view.fragment
import android.app.AlertDialog

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
import com.example.lab.adapter.data.SeatStatus
import com.example.lab.application.MyApplication
import com.example.lab.databinding.FragmentReservResultBinding
import com.example.lab.databinding.SubSeatGridviewBinding
import com.example.lab.utils.DateManager
import com.example.lab.utils.DensityManager
import com.example.lab.utils.extension.*
import com.example.lab.viewmodel.ReservViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
        reservVM = ViewModelProvider(this)[ReservViewModel::class.java]

        /** 데이터를 관리하는 뷰 모델을 bind에 연결해줘야 적용 됨 */
        bind.lifecycleOwner = requireActivity()

        this.apply {
            hideTitleBar()
            hideNavBar()
        }

        /** 데이터를 관리하는 뷰 모델을 bind에 연결해줘야 적용 됨 */
        initView()
        initGridView()
        addOkBtnClickEvent()
        initReservData()
        addObserver()

        return bind.root
    }

    private fun addObserver(){
        reservVM.apply {
            cancleResult.observe(viewLifecycleOwner){
                this@ReservResultFragment.showOkAlert("예약 취소 완료", "예약이 취소 되었습니다.")
            }
            cancleError.observe(viewLifecycleOwner){
                this@ReservResultFragment.showOkAlert("예약 취소 실패", it)
            }

            extendResult.observe(viewLifecycleOwner){
                this@ReservResultFragment.showOkAlert("예약 연장 완료", "예약이 연장 되었습니다.")
            }

            extendError.observe(viewLifecycleOwner){
                this@ReservResultFragment.showOkAlert("예약 연장 실패", it)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView(){
        bind.todayTV.text = DateManager.getDateUntilDate(Calendar.getInstance().timeInMillis)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun initReservData(){
        reservVM.getUserReservs(MyApplication.member?.userId!!)

        reservVM.reservList.observe(requireActivity()){ result ->
            if(result.reservList.isNotEmpty()){
                bind.seatGridView.blurFrameLayout.visibility = View.GONE

                result.reservList.last().let {
                    bind.apply {
                        labNumber.text = "정보공학관 ${it.roomNumber}호"
                        studentNameTv.text = "${it.name}(${it.userId})"
                        majorTv.text = it.major
                        replaceTv.text = "정보공학관 ${it.roomNumber}호"
                        timeTv.text = "${DateManager.dateParse(it.startTime)}-${DateManager.dateParse(it.endTime)}"
                        seatTv.text = "${it.seatNum}번 좌석"
                        extendableTv.text = DateManager.dateParse(it.extendableTime)
                        seatGridView.infoLayout.visibility = View.GONE

                        permissionTv.apply { 
                            text = if(it.permission){
                                setTextColor(resources.getColor(R.color.blue))
                                permissionLayout.setCardBackgroundColor(resources.getColor(R.color.light_blue))
                                cancleBtn.setOnClickListener{ _ ->
                                    val alertDialog: AlertDialog? = activity?.let { frag->
                                        val builder = AlertDialog.Builder(frag)
                                        builder.apply {
                                            setTitle("예약 연장")
                                            setMessage("예약을 연장 하시겠습니까?")
                                            setPositiveButton("확인") { dialog, _ ->
                                                dialog.dismiss()
                                                reservVM.extendReserv(it.id)
                                            }
                                            setNegativeButton("취소") {dialog, _ -> dialog.dismiss()}
                                        }
                                        builder.create()
                                    }
                                    alertDialog?.show()
                                }
                                cancleBtn.text = "연장"
                                "승인 완료"
                            } else{
                                setTextColor(resources.getColor(R.color.black_gray))
                                permissionLayout.setCardBackgroundColor(resources.getColor(R.color.gray))
                                cancleBtn.setOnClickListener{ _ ->
                                    val alertDialog: AlertDialog? = activity?.let { frag->
                                        val builder = AlertDialog.Builder(frag)
                                        builder.apply {
                                            setTitle("예약 취소")
                                            setMessage("예약을 취소하시겠습니까?")
                                            setPositiveButton("확인") { dialog, _ ->
                                                dialog.dismiss()
                                                reservVM.cancleReserv(it.id)
                                            }
                                            setNegativeButton("취소") {dialog, _ -> dialog.dismiss()}
                                        }
                                        builder.create()
                                    }
                                    alertDialog?.show()
                                }
                                cancleBtn.text = "취소"
                                "승인 대기"
                            }
                        }

                        // 예약한 좌석만 표시
                        seatGridView.apply {
                            for (i in 0 until LAB_SEAT_SIZE / 2) {
                                leftSeatGridView.markSeatInUser(arrayListOf(it.seatNum.toInt()), i)
                                rightSeatGridView.markSeatInUser(arrayListOf(it.seatNum.toInt()), i)
                            }
                        }
                    }
                }
            } else{
                bind.seatGridView.blurFrameLayout.visibility = View.VISIBLE
            }
        }
    }

    /**
     * 확인 버튼 클릭 이벤트
     */
    private fun addOkBtnClickEvent(){
        bind.okBtn.setOnClickListener{
            this.dismiss()
        }
    }

    private fun initGridView(){
        // 그리드뷰를 include로 불러왔으므로 include한 레이아웃을 먼저 가져옴
        val seatGridView: SubSeatGridviewBinding = bind.seatGridView.apply {
            blurFrameLayout.visibility = View.VISIBLE // 블러뷰 처음엔 보이게 설정
            blurViewText.text = "예약이 없습니다."
        }

        bind.todayTimeTV.text = dateFormat.format(Calendar.getInstance().timeInMillis)

        val leftSeatList: ArrayList<SeatStatus> = arrayListOf()
        val rightSeatList: ArrayList<SeatStatus> = arrayListOf()

        // 좌석 번호 세팅
        var flag = true
        for (i in 1..32){
            if(flag) leftSeatList.add(SeatStatus(i))
            else rightSeatList.add(SeatStatus(i))

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
                    height = seatGridView.labSeatLayout.height + DensityManager.convertDPtoPX(10)
                }

                seatGridView.labSeatLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    /**
     * 취소 버튼 클릭 이벤트 리스너
     */
    private fun getCancleListener(reservId:Int):View.OnClickListener {
        return View.OnClickListener {
            this.showOkCancleAlert("예약 취소", "예약을 취소하시겠습니까 ?", reservVM.cancleReserv(reservId))
        }
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()

        this.apply {
            showTitleBar()
            showNavBar()
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
        private const val LAB_SEAT_SIZE = 32 // 실습실 좌석 총 개수
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