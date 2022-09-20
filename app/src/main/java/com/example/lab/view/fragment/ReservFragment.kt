package com.example.lab.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.lab.R
import com.example.lab.adapter.SeatAdapter
import com.example.lab.databinding.FragmentReservBinding
import com.example.lab.databinding.SubSeatGridviewBinding

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

    private lateinit var prevSelectSeat:View
    private lateinit var bind:FragmentReservBinding

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
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_reserv, container, false)

        /** 데이터를 관리하는 뷰 모델을 bind에 연결해줘야 적용 됨 */
        bind.lifecycleOwner = requireActivity()

        initGridView()
        initSpinner()

        return bind.root
    }

    private fun initSpinner(){
        // ArrayAdapter.createFromResource가 ArrayAdapter를 반환하므로 also로 adapter를 초기화한 후 할당
        bind.teamSelector.adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.team_list,
            R.layout.spinner_item_team
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun initGridView(){
        // 그리드뷰를 include로 불러왔으므로 include한 레이아웃을 먼저 가져옴
        var seatGridView: SubSeatGridviewBinding = bind.seatGridView

        var leftSeatList: MutableList<Int> = mutableListOf()
        var rightSeatList: MutableList<Int> = mutableListOf()

        // 좌석 번호 세팅
        var flag = true
        for (i in 1..32){
            if(flag){
                Log.i("LEFT", "$i")
                leftSeatList.add(i)
            }
            else{
                Log.i("RIGHT", "$i")
                rightSeatList.add(i)
            }

            if(i % 4 == 0) flag = !flag
        }

        var leftSeatAdapter: SeatAdapter = SeatAdapter(context = requireContext(), leftSeatList)
        var rightSeatAdapter: SeatAdapter = SeatAdapter(context = requireContext(), rightSeatList)

        seatGridView.leftSeatGridView.adapter = leftSeatAdapter
        seatGridView.rightSeatGridView.adapter = rightSeatAdapter

        seatGridView.leftSeatGridView.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, l ->
                // 이전에 선택했던 자리는 다시 회색으로 돌림
                if (::prevSelectSeat.isInitialized) {
                    prevSelectSeat.background = resources.getDrawable(R.drawable.shape_seat)
                }

                var seat = view.findViewById(R.id.seat) as View
                prevSelectSeat = seat

                seat.background = resources.getDrawable(R.drawable.shape_seat_selected)

                Toast.makeText(
                    requireContext(),
                    "${leftSeatList[position]} 번 좌석",
                    Toast.LENGTH_SHORT
                ).show()
            }

        seatGridView.rightSeatGridView.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, l ->
                // 이전에 선택했던 자리는 다시 회색으로 돌림
                if (::prevSelectSeat.isInitialized) {
                    prevSelectSeat.background = resources.getDrawable(R.drawable.shape_seat)
                }

                var seat = view.findViewById(R.id.seat) as View
                prevSelectSeat = seat

                seat.background = resources.getDrawable(R.drawable.shape_seat_selected)

                Toast.makeText(
                    requireContext(),
                    "${rightSeatList[position]} 번 좌석",
                    Toast.LENGTH_SHORT
                ).show()
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