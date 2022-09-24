package com.example.lab.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.lab.R
import com.example.lab.databinding.FragmentTimeTableBinding
import com.example.lab.view.bottomsheet.AddClassFragment
import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.Time
import com.github.tlaabs.timetableview.TimetableView

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

        initTimeTable()
        initLabSpinner()

        bind.addClassBtn.setOnClickListener{
            val bottomSheet = AddClassFragment()
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }

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

    private fun initTimeTable(){
        var schedules:ArrayList<Schedule> = arrayListOf()

        schedules.add(createSchedule("객체지향모델링", "912", "장희숙", 0, Time(9, 0), Time(11, 0)))
        schedules.add(createSchedule("객체지향모델링", "915", "장희숙", 2, Time(11, 0), Time(13, 0)))
        bind.timetable.add(schedules)
        schedules.clear()

        schedules.add(createSchedule("데이터베이스 응용", "914", "이중화", 1, Time(13, 0), Time(15, 0)))
        schedules.add(createSchedule("데이터베이스 응용", "915", "이중화", 2, Time(9, 0), Time(11, 0)))
        bind.timetable.add(schedules)
        schedules.clear()

        schedules.add(createSchedule("컴퓨터알고리즘", "914", "권오준", 0, Time(12, 0), Time(13, 0)))
        schedules.add(createSchedule("컴퓨터알고리즘", "915", "권오준", 3, Time(9, 0), Time(11, 0)))
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