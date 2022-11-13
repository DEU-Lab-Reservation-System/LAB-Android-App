package com.example.lab.view.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.lab.R
import com.example.lab.data.responseDto.ReservResponseDto
import com.example.lab.databinding.FragmentReservCompleteBinding
import com.example.lab.utils.DateManager
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReservResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReservCompleteFragment : Fragment() {

    private var argument:String? = null

    // VARIABLE
    private lateinit var bind: FragmentReservCompleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            argument = it.getString("ReservInfo")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // 데이터 바인딩
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_reserv_complete, container, false)

        /** 데이터를 관리하는 뷰 모델을 bind에 연결해줘야 적용 됨 */
        bind.lifecycleOwner = requireActivity()

        addButtonEvent()
        setReservData()

        return bind.root
    }

    /**
     * 예약 신청 결과를 표시하는 메소드
     */
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setReservData(){
        if(argument == null) return

        val reservInfo = ReservResponseDto.Reserv.createDto(JSONObject(argument))

        bind.apply {
            reservInfo.apply {
                userNameTv.text = "${name}(${userId})"
                majorTv.text = major
                placeTv.text = "정보공학관 $roomNumber"
                seatTv.text = "${seatNum}번 좌석"
                timeTv.text = "${DateManager.dateParse(startTime)}-${DateManager.dateParse(endTime)}"
            }
        }
    }

    private fun addButtonEvent(){
        bind.nextBtn.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit();
            requireActivity().supportFragmentManager.popBackStack();
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReservResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReservCompleteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}