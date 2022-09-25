package com.example.lab.view.fragment

import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.lab.R
import com.example.lab.adapter.SeatAdapter
import com.example.lab.databinding.FragmentReservBinding
import com.example.lab.databinding.SubSeatGridviewBinding
import java.util.*

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
    private lateinit var lablist:Array<String>

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
        addTimePicker()
        addEventreservationBtn()

        return bind.root
    }

    private fun addEventreservationBtn(){
        bind.reservBtn.setOnClickListener(View.OnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, NoticeFragment())
                .addToBackStack(null)
                .commit()

        })
    }

    private fun initSpinner(){
        // ArrayAdapter.createFromResource가 ArrayAdapter를 반환하므로 also로 adapter를 초기화한 후 할당
        bind.teamSelector.adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.team_list,
            R.layout.spinner_custom_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        lablist = resources.getStringArray(R.array.lab_list)
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, lablist)

        // 어댑터 등록
        bind.labSelector.adapter = spinnerAdapter
        bind.labSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                bind.seatGridView.labNumber.text = "${lablist[position]}호 좌석 현황"
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    /** 실습실 선택 스피너 초기화 */
    private fun initLabSpinner(){
        // 스피너는 고정된 리스트를 보여주는 것이기 때문에 xml로 따로 관리하는 것이 좋음
        lablist = resources.getStringArray(R.array.lab_list)


    }

    private fun addTimePicker(){
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR)

        bind.startTimeEditText.setOnClickListener{
            val timePicker = TimePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, { timePicker, selectHour, selectMinute ->
                bind.startTimeEditText.setText(String.format("%02d:%02d", selectHour, selectMinute))
            }, hour, 0, true)

            timePicker.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            timePicker.show()
        }

        bind.endTimeEditText.setOnClickListener{
            val timePicker = TimePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, { timePicker, selectHour, selectMinute ->
                bind.endTimeEditText.setText(String.format("%02d:%02d", selectHour, selectMinute))
            }, hour, 0, true)

            timePicker.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            timePicker.show()
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
            if(flag) leftSeatList.add(i)
            else rightSeatList.add(i)

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
                bind.selectSeatTV.text = "${leftSeatList[position]} 번"
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
                bind.selectSeatTV.text = "${rightSeatList[position]} 번"
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