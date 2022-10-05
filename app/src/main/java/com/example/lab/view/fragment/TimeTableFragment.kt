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
import com.example.lab.utils.CustomTimeTableView
import com.example.lab.utils.DateManager
import com.example.lab.view.bottomsheet.AddClassFragment
import com.example.lab.view.bottomsheet.ClassInfoFragment
import com.example.lab.viewmodel.LectureViewModel
import com.github.tlaabs.timetableview.Schedule
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
class TimeTableFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // VARIABLE
    private lateinit var bind:FragmentTimeTableBinding
    private lateinit var lectureVM:LectureViewModel

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

        initTimeTableSchedule()
        initLabSpinner()
        addClickEventToSticker()
        addClassBtnEventListener()


        return bind.root
    }

    private fun initLabSpinner(){
        // 어댑터 등록
        val lablist = resources.getStringArray(R.array.lab_list)

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, lablist)

        bind.labSelector.adapter = spinnerAdapter
        bind.labSelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {}
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    /** 수업 추가 버튼 이벤트
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
                var schedule:ArrayList<Schedule> = bind.timetable.stickers[idx]!!.schedules

                val classPlaceInfo = StringJoiner(", ")
                val classTimeInfo = StringBuilder()

                schedule.forEach {
                    classTimeInfo.append(String.format(" %s %02d:%02d~%02d:%02d", DateManager.day(it.day), it.startTime.hour, it.startTime.minute, it.endTime.hour, it.endTime.minute))
                    classPlaceInfo.add("${it.classPlace}")

                    Log.i("수업 이름", it.classTitle)
                    Log.i("수업 장소", it.classPlace)
                    Log.i("담당 교수", it.professorName)
                    Log.i("요일", "${it.day}")
                    Log.i("시작 시간", "${it.startTime.hour}")
                    Log.i("종료 시간", "${it.endTime.hour}")
                }

                val bottomSheet = ClassInfoFragment()
                var args = Bundle()

                args.apply {
                    putInt("index", idx)
                    putString("classTitle", schedule[0].classTitle)
                    putString("professor", schedule[0].professorName)
                    putString("classTime", classTimeInfo.toString())
                    putString("classPlace", classPlaceInfo.toString())
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

    /** 시간표 초기화 메소드 */
    private fun initTimeTableSchedule(){
        var schedules:ArrayList<Schedule> = arrayListOf()

        schedules.add(createSchedule("객체지향모델링", "정보공학관 912", "장희숙", 0, Time(9, 0), Time(11, 0)))
        schedules.add(createSchedule("객체지향모델링", "정보공학관 915", "장희숙", 2, Time(11, 0), Time(13, 0)))
        bind.timetable.add(schedules)
        schedules.clear()

        schedules.add(createSchedule("데이터베이스 응용", "정보공학관 914", "이중화", 1, Time(13, 0), Time(15, 0)))
        schedules.add(createSchedule("데이터베이스 응용", "정보공학관 915", "이중화", 2, Time(9, 0), Time(11, 0)))
        bind.timetable.add(schedules)
        schedules.clear()

        schedules.add(createSchedule("컴퓨터알고리즘", "정보공학관 914", "권오준", 0, Time(12, 0), Time(13, 0)))
        schedules.add(createSchedule("컴퓨터알고리즘", "정보공학관 915", "권오준", 3, Time(9, 0), Time(11, 0)))
        bind.timetable.add(schedules)
        schedules.clear()

        schedules.add(createSchedule("컴퓨터알고리즘", "정보공학관 914", "권오준", 0, Time(13, 0), Time(15, 0)))
        schedules.add(createSchedule("컴퓨터알고리즘", "정보공학관 915", "권오준", 1, Time(9, 0), Time(11, 0)))
        bind.timetable.add(schedules)
        schedules.clear()

        schedules.add(createSchedule("컴퓨터알고리즘", "정보공학관 914", "권오준", 4, Time(9, 0), Time(13, 0)))
        schedules.add(createSchedule("컴퓨터알고리즘", "정보공학관 915", "권오준", 3, Time(12, 0), Time(14, 0)))
        bind.timetable.add(schedules)
        schedules.clear()

        schedules.add(createSchedule("동아리캡스톤", "정보공학관 914", "박유현", 4, Time(15, 0), Time(17, 0)))
        bind.timetable.add(schedules)
        schedules.clear()
    }

    private fun createSchedule(title:String, place:String, professor:String, day:Int, startTime:Time, endTime:Time):Schedule{
        return Schedule().apply {
            this.classTitle = title
            this.classPlace = place
            this.professorName = professor
            this.day = day
            this.startTime = startTime
            this.endTime = endTime
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