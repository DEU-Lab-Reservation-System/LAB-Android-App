package com.example.lab.view.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.lab.R
import com.example.lab.data.entity.Lecture
import com.example.lab.databinding.FragmentTimeTableBinding
import com.example.lab.custom.timetableview.CustomTimeTableView
import com.example.lab.custom.timetableview.Schedule
import com.example.lab.utils.DateManager
import com.example.lab.view.bottomsheet.AddClassFragment
import com.example.lab.view.bottomsheet.ClassInfoFragment
import com.example.lab.viewmodel.LectureViewModel
import com.github.tlaabs.timetableview.Time
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TimeTableFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@RequiresApi(Build.VERSION_CODES.O)
class TimeTableFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // VARIABLE
    private lateinit var bind:FragmentTimeTableBinding
    private lateinit var lectureVM:LectureViewModel
    private lateinit var lablist:Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment

        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_time_table, container, false)

        lectureVM = ViewModelProvider(requireActivity())[LectureViewModel::class.java]

        initTimeTable()
        initLabSpinner()
        addClickEventToSticker()
        addClassBtnEventListener()

        return bind.root
    }

    /**
     * 실습실 선택 스피너 초기화 메소드
     */
    private fun initLabSpinner(){
        lablist = resources.getStringArray(R.array.lab_list)

        // 어댑터 등록
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, lablist)

        bind.labSelector.adapter = spinnerAdapter
        bind.labSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setLabTimeTable(lablist[position])
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    /**
     * 수업 추가 버튼 이벤트
     * AddClassFragment (바텀 시트)의 인터페이스를 해당 프래그먼트에서 구현함으로써 데이터를 전달 받음
     */
    private fun addClassBtnEventListener(){
        bind.addClassBtn.setOnClickListener{
            val bottomSheet = AddClassFragment()
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)

            bottomSheet.dataReciever = object : AddClassFragment.BottomSheedDataReciever{
                @RequiresApi(Build.VERSION_CODES.N)
                override fun setClassDatas(lectureList: ArrayList<Lecture>) {
                    lectureList.forEach{
                        Log.i("수업 정보", it.toString())
                    }

                    // 수업 등록
                    lectureVM.addLecture(lectureList)
                }
            }
        }
    }

    /** 시간표 스티커(수업) 클릭 이벤트 등록 메소드 */
    private fun addClickEventToSticker(){
        /** 시간표 스티커 클릭 이벤트 (수업 정보 출력) */
        bind.timetable.setOnStickerSelectEventListener(object : CustomTimeTableView.OnStickerSelectedListener{
            @RequiresApi(Build.VERSION_CODES.N)
            override fun OnStickerSelected(idx: Int, schedules: java.util.ArrayList<Schedule>?) {
                var schedule:ArrayList<Schedule> = bind.timetable.stickers[idx]!!.getSchedules()

                val classPlaceInfo = StringJoiner(", ")
                val classTimeInfo = StringBuilder()

                schedule.forEach {
                    classTimeInfo.append(String.format(" %s %02d:%02d~%02d:%02d", DateManager.day(it.day), it.startTime.hour, it.startTime.minute, it.endTime.hour, it.endTime.minute))
                    classPlaceInfo.add("${it.classPlace}")

                    Log.i("", it.toString())
                }

                val bottomSheet = ClassInfoFragment()
                var args = Bundle()

                args.apply {
                    putInt("index", idx)
                    putString("classTitle", schedule[0].classTitle)
                    putString("professor", schedule[0].professorName)
                    putString("classTime", classTimeInfo.toString())
                    putString("classPlace", classPlaceInfo.toString())

                    /** 같은 수업 정보를 전부 표시해줘야하므로 putStringArrayList()로 전달하기 */
                }

                bottomSheet.arguments = args
                bottomSheet.show(parentFragmentManager, bottomSheet.tag)


                /** 시간표 수정 코드
                 *  edit : bind.timetable.stickers[idx]의 schedules를 다시 그려줌
                 *  즉, 수정하려면 기존의 수업(schedule)을 다 불러와서 변경할 부분만 바꾼 후 다시 다 넣어주면 됨
                 *
                schedules!!.add(bind.timetable.stickers[idx]!!.schedules[0].apply {
                    classTitle = "수정 됨"
                })

                bind.timetable.edit(idx, schedules)
                */
            }
        })
    }

    /**
     * 특정 실습실의 시간표를 가져오는 메소드
     */
    private fun setLabTimeTable(labId:String){
        bind.timetable.removeAll()

        val lectureList:Map<String?, List<Lecture>> = lectureVM.getLabsLectures(labId)
        var schedules:ArrayList<Schedule> = arrayListOf()

        lectureList.forEach {
            it.value.forEach { lecture ->
                schedules.add(Schedule.createSchedule(lecture))
                Log.i("수업 추가", Schedule.createSchedule(lecture).toString())
            }
            bind.timetable.add(schedules)
            schedules.clear()
        }
    }

    /**
     * 시간표 초기화 메소드
     */
    private fun initTimeTable(){
        lectureVM.getAllLectures()

        lectureVM.lectureHash.observe(requireActivity()){
            setLabTimeTable("911")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TimeTableFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TimeTableFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}