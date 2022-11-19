package com.example.lab.view.bottomsheet

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab.R
import com.example.lab.application.MyApplication
import com.example.lab.data.entity.Lecture
import com.example.lab.data.enum.Role
import com.example.lab.databinding.BottomsheetManageClassBinding
import com.example.lab.utils.DateManager
import com.example.lab.view.viewinitializer.ClassBottomSheetManager
import com.example.lab.viewmodel.LectureViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddClassFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddClassFragment : BottomSheetDialogFragment() {

    // VARIABLE
    private lateinit var lectureVM: LectureViewModel
    private lateinit var bind: BottomsheetManageClassBinding
    private lateinit var classManager: ClassBottomSheetManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bind = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_manage_class, container, false)
        lectureVM = ViewModelProvider(this)[LectureViewModel::class.java]
        classManager = ClassBottomSheetManager(bind, inflater, requireContext())

        classManager.addDatePicker()
        addEventClassInfoBtn(container)
        // 확인 버튼 클릭 이벤트
        addCompleteBtnEvent()
        initView()
        return bind.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView(){
        MyApplication.member?.let {
            if(it.role == Role.ADMIN){ // 조교인 경우는 학기 시작일과 종료일로 고정
                val today = DateManager.getTodayUntilDate()
                val year = Calendar.getInstance().weekYear
                bind.apply {
                    startDateEditText.setOnClickListener(null)
                    endDateEditText.setOnClickListener(null)
                }

                if(today > "${year}-09-01"){
                    bind.apply {
                        startDateEditText.setText("${year}-09-01")
                        endDateEditText.setText("${year}-12-31")
                    }
                } else {
                    bind.apply {
                        startDateEditText.setText("${year}-03-01")
                        endDateEditText.setText("${year}-06-31")
                    }
                }
            } else { // 교수인 경우 날짜 선택 가능하게
                classManager.addDatePicker()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val metrics = resources.displayMetrics

        // 바텀 시트의 최상위 Layout의 Height를 기기의 Height로 설정해줘야 위로 드래그 했을 때 FullScreen 가능
        val layout = view.findViewById<View>(R.id.parentLayout)
        layout.layoutParams.height = metrics.heightPixels
    }

    /** 수업 추가 버튼 이벤트 */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun addCompleteBtnEvent(){
        bind.completeBtn.setOnClickListener{
            // 수업 코드 생성
            val classCode = MyApplication.member?.let {
                if(Role.PROF == it.role) it.userId
                else UUID.randomUUID().toString().substring(0 until 8)
            }

            // 입력한 수업의 정보 받아오기
            val lectureList = classManager.getInputClassData(classCode!!)
            // 입력된 수업 정보 확인
            if (inputCheck(lectureList)){
                lectureVM.addLecture(lectureList)
            } else {
                val alertDialog: AlertDialog? = activity?.let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        setTitle("수업 추가 오류")
                        setMessage("입력 값을 확인해주세요")
                        setPositiveButton("확인") { dialog, _ -> dialog.dismiss()}
                    }
                    builder.create()
                }
                alertDialog?.show()
            }
        }

        lectureVM.addLectureFlag.observe(viewLifecycleOwner) {
            AlertDialog.Builder(requireContext()).apply {
                // 수업 추가 성공 시
                if (it) {
                    setTitle("수업 추가 완료")
                    setMessage("수업이 추가 되었습니다.")
                    setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                        this@AddClassFragment.dismiss()
                    }
                } else { // 수업 추가 실패 시
                    setTitle("수업 추가 실패")
                    setMessage("수업 추가 중 오류가 발생했습니다. 입력 값을 확인해주세요.")
                    setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                }
                create()
                show()
            }
            /**
             * AlertDialog가 두번 뜨는 것을 방지하기 위해 다이얼로그 띄우고 바로 옵저버 제거
             * 버튼 누를 때마다 옵저버를 등록 시킴
             */
            //lectureVM.addLectureFlag.removeObservers(this@AddClassFragment)
        }
    }

    private fun inputCheck(lectureList: ArrayList<Lecture>): Boolean{
        if(lectureList.isEmpty()) return false
        lectureList.forEach {
            if(it.startTime >= it.endTime) return false
            if(it.startDate >= it.endDate) return false
            if(it.title.isEmpty() || it.professor.isEmpty()) return false
        }
        return true
    }

    /** 시간 및 장소 레이아웃 추가 버튼 이벤트 메소드 */
    private fun addEventClassInfoBtn(container: ViewGroup?){
        bind.addClassInfoBtn.setOnClickListener{
            classManager.addClassInfoLayout(container)
        }
    }

}