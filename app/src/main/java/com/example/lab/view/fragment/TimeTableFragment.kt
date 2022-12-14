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
import com.example.lab.application.MyApplication
import com.example.lab.data.entity.Lecture
import com.example.lab.databinding.FragmentTimeTableBinding
import com.example.lab.custom.timetableview.CustomTimeTableView
import com.example.lab.custom.timetableview.Schedule
import com.example.lab.view.bottomsheet.AddClassFragment
import com.example.lab.view.bottomsheet.ClassInfoFragment
import com.example.lab.view.viewinitializer.ViewInitializer
import com.example.lab.view.viewinitializer.ViewInitializerFactory
import com.example.lab.viewmodel.LectureViewModel
import kotlin.collections.ArrayList
import kotlin.streams.toList

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
class TimeTableFragment : Fragment(){
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

        MyApplication.member?.let {
            ViewInitializerFactory().getInitializer(it.role, "TIMETABLE").init(this, bind)
        }

        initTimeTable()
        initLabSpinner()
        addClickEventToSticker()
        addClassBtnEventListener()
        addSwipeRefreshEvent()

        return bind.root
    }

    /**
     * ????????? ?????? ????????? ????????? ?????????
     */
    private fun initLabSpinner(){
        lablist = resources.getStringArray(R.array.lab_list)

        // ????????? ??????
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
     * ?????? ?????? ?????? ?????????
     *
     * AddClassFragment ?????? ?????? ??????
     */
    private fun addClassBtnEventListener(){
        bind.addClassBtn.setOnClickListener{
            val bottomSheet = AddClassFragment()
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }
    }


    /**
     * ????????? ????????? ?????????
     */
    private fun initTimeTable(){
        lectureVM.getAllLectures()

        lectureVM.lectureHash.observe(requireActivity()){
            setLabTimeTable("${bind.labSelector.selectedItem}")
        }
    }

    /**
     * ?????? ???????????? ???????????? ???????????? ?????????
     */
    private fun setLabTimeTable(labId:String){
        // ?????? ???????????? ??????
        bind.timetable.removeAll()

        val lectureList:Map<String?, List<Lecture>> = lectureVM.getLabsLectures(labId)

        val schedules:ArrayList<Schedule> = arrayListOf()

        lectureList.forEach {
            it.value.forEach { lecture ->
                schedules.add(Schedule.createSchedule(lecture))
                Log.i("?????? ??????", Schedule.createSchedule(lecture).toString())
            }
            bind.timetable.add(schedules)
            schedules.clear()
        }
    }

    /**
     * ????????? ?????????(??????) ?????? ????????? ?????? ?????????
     */
    private fun addClickEventToSticker(){
        /** ????????? ????????? ?????? ????????? (?????? ?????? ??????) */
        bind.timetable.setOnStickerSelectEventListener(object : CustomTimeTableView.OnStickerSelectedListener{

            override fun OnStickerSelected(idx: Int, schedules: java.util.ArrayList<Schedule>?) {
                val schedule:ArrayList<Schedule> = bind.timetable.stickers[idx]!!.getSchedules()
                
                // ????????? ????????? ?????? ????????? ?????? ?????? ????????? ????????? ??? Schedule ???????????? ??????
                val clickSchdules = lectureVM.getLectures(schedule[0].code!!)?.stream()
                    ?.map { lecture -> Schedule.createSchedule(lecture) }?.toList() as ArrayList<Schedule>

                // ????????? ????????? ????????? JSON?????? ?????? ??? ??????
                val bottomSheet = ClassInfoFragment()
                bottomSheet.arguments = Bundle().apply {
                    putInt("index", idx)
                    putString("classInfoJson", Schedule.toJson(clickSchdules).toString())
                }

                bottomSheet.show(parentFragmentManager, bottomSheet.tag)

                /** ????????? ?????? ??????
                 *  edit : bind.timetable.stickers[idx]??? schedules??? ?????? ?????????
                 *  ???, ??????????????? ????????? ??????(schedule)??? ??? ???????????? ????????? ????????? ?????? ??? ?????? ??? ???????????? ???
                 *
                schedules!!.add(bind.timetable.stickers[idx]!!.schedules[0].apply {
                    classTitle = "?????? ???"
                })

                bind.timetable.edit(idx, schedules)
                */
            }
        })
    }

    /**
     * SwipeRefreshLayout??? ????????? ??? ???????????? ?????????
     */
    private fun addSwipeRefreshEvent(){
        bind.swipeLayout.setOnRefreshListener {
            initTimeTable()

            bind.swipeLayout.isRefreshing = false
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