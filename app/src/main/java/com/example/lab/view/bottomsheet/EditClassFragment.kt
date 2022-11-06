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
import com.example.lab.custom.timetableview.Schedule
import com.example.lab.databinding.BottomsheetManageClassBinding
import com.example.lab.utils.DateManager
import com.example.lab.view.viewmanager.ClassBottomSheetManager
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
        lectureVM = ViewModelProvider(requireActivity())[LectureViewModel::class.java]

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
            
            // 전달 받은 수업 정보를 레이아웃에 추가
            // 수업 추가 정보 레이아웃 생성
            schedules.forEach { schedule ->
                classManager.addClassInfoLayout(
                    container,
                    DateManager.day(schedule.day),
                    "정보공학관 ${schedule.classPlace}",
                    schedule.startTime,
                    schedule.endTime
                )
            }
        }
    }


    /** 수업 수정 버튼 이벤트 */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun addCompleteBtnEvent(){
        bind.completeBtn.setOnClickListener {
            val lectureList = classManager.getInputClassData(schedules[0].code!!)

            lectureVM.editLecture(lectureList)

            lectureVM.editLectureFlag.observe(requireActivity()){
                AlertDialog.Builder(requireContext()).apply {
                    if(it){
                        setTitle("수업 수정 완료")
                        setMessage("수정이 완료 되었습니다.")
                        setPositiveButton("OK"){ dialog, _ ->
                            dialog.dismiss()
                            this@EditClassFragment.dismiss()
                        }
                    } else {
                        setTitle("수업 수정 실패")
                        setMessage("수정 중 오류가 발생했습니다.. 입력 값을 확인해주세요.")
                        setPositiveButton("OK"){ dialog, _ -> dialog.dismiss() }
                    }
                    create()
                    show()
                }
            }

//            Log.i("수정 전 수업 정보", schedules.toString())
//            Log.i("수정 된 수업 정보", lectureList.toString())
        }
    }

    /**
     * 수정 모드로 레이아웃을 설정
     * 기존 수업 정보를 세팅
     */
    private fun setEditLayout(
        title: String,
        professor: String,
        startDate: String,
        endDate: String
    ){
        bind.layoutTitleTextView.text = "시간표 수정"
        bind.titleEditText.setText(title)
        bind.professorEditText.setText(professor)
        bind.startDateEditText.setText(startDate)
        bind.endDateEditText.setText(endDate)
    }

    /** 시간 및 장소 추가 버튼 이벤트 메소드 */
    private fun addClassInfoBtnEvent(container: ViewGroup?){
        bind.addClassInfoBtn.setOnClickListener{
            classManager.addClassInfoLayout(container)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val metrics = resources.displayMetrics

        // 바텀 시트의 최상위 Layout의 Height를 기기의 Height로 설정해줘야 위로 드래그 했을 때 FullScreen 가능
        val layout = view.findViewById<View>(R.id.parentLayout)
        layout.layoutParams.height = metrics.heightPixels
    }
}