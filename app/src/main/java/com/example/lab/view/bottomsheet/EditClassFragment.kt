package com.example.lab.view.bottomsheet
import android.app.AlertDialog

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.lab.R
import com.example.lab.application.MyApplication
import com.example.lab.custom.timetableview.Schedule
import com.example.lab.databinding.BottomsheetManageClassBinding
import com.example.lab.utils.DateManager
import com.example.lab.view.viewinitializer.ClassBottomSheetManager
import com.example.lab.viewmodel.LectureViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONObject

class EditClassFragment : BottomSheetDialogFragment() {
    // VARIABLE
    private lateinit var bind: BottomsheetManageClassBinding
    private lateinit var classManager: ClassBottomSheetManager
    private lateinit var schedules: ArrayList<Schedule>
    private lateinit var lectureVM: LectureViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_manage_class, container, false)
        lectureVM = ViewModelProvider(this)[LectureViewModel::class.java]

        classManager = ClassBottomSheetManager(bind, inflater, requireContext())
        classManager.addDatePicker()


        addClassInfoBtnEvent(container)
        initClassData(arguments, container)
        addCompleteBtnEvent()

        return bind.root
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initClassData(arguments: Bundle?, container: ViewGroup?){
        arguments?.getString("classInfoJson")?.let { it ->
            schedules = Schedule.toScheduleList(JSONObject(it))

            schedules[0].let {
                setEditLayout(it.classTitle, it.professorName, it.startDate, it.endDate)
            }
            
            // ?????? ?????? ?????? ????????? ??????????????? ??????
            // ?????? ?????? ?????? ???????????? ??????
            schedules.forEach { schedule ->
                classManager.addClassInfoLayout(
                    container,
                    DateManager.day(schedule.day),
                    "??????????????? ${schedule.classPlace}",
                    schedule.startTime,
                    schedule.endTime
                )
            }
        }
    }


    /** ?????? ?????? ?????? ????????? */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun addCompleteBtnEvent(){
        bind.completeBtn.setOnClickListener {
            val lectureList = classManager.getInputClassData(schedules[0].code!!)

            lectureVM.editLecture(lectureList)
        }

        lectureVM.editLectureFlag.observe(viewLifecycleOwner){
            AlertDialog.Builder(requireContext()).apply {
                if(it){
                    setTitle("?????? ?????? ??????")
                    setMessage("????????? ?????? ???????????????.")
                    setPositiveButton("OK"){ dialog, _ ->
                        dialog.dismiss()
                        this@EditClassFragment.dismiss()
                    }
                } else {
                    setTitle("?????? ?????? ??????")
                    setMessage("?????? ??? ????????? ??????????????????. ?????? ?????? ??????????????????.")
                    setPositiveButton("OK"){ dialog, _ -> dialog.dismiss() }
                }
                create()
                show()
            }
        }
    }

    /**
     * ?????? ????????? ??????????????? ??????
     * ?????? ?????? ????????? ??????
     */
    private fun setEditLayout(
        title: String,
        professor: String,
        startDate: String,
        endDate: String
    ){
        bind.layoutTitleTextView.text = "????????? ??????"
        bind.titleEditText.setText(title)
        bind.professorEditText.setText(professor)
        bind.startDateEditText.setText(startDate)
        bind.endDateEditText.setText(endDate)
    }

    /** ?????? ??? ?????? ?????? ?????? ????????? ????????? */
    private fun addClassInfoBtnEvent(container: ViewGroup?){
        bind.addClassInfoBtn.setOnClickListener{
            classManager.addClassInfoLayout(container)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val metrics = resources.displayMetrics

        // ?????? ????????? ????????? Layout??? Height??? ????????? Height??? ??????????????? ?????? ????????? ?????? ??? FullScreen ??????
        val layout = view.findViewById<View>(R.id.parentLayout)
        layout.layoutParams.height = metrics.heightPixels
    }
}