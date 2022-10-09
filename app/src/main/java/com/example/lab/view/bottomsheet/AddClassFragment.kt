package com.example.lab.view.bottomsheet

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.lab.R
import com.example.lab.data.entity.Lecture
import com.example.lab.databinding.BottomsheetManageClassBinding
import com.example.lab.view.viewmanager.BottomSheetDataReceiver
import com.example.lab.view.viewmanager.ClassBottomSheetManager
import com.example.lab.viewmodel.LectureViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_manage_class, container, false)
        lectureVM = ViewModelProvider(requireActivity())[LectureViewModel::class.java]
        classManager = ClassBottomSheetManager(bind, inflater, requireContext())

        classManager.addDatePicker()
        addEventClassInfoBtn(container)

        // 확인 버튼 클릭 이벤트
        addCompleteBtnEvent()

        return bind.root
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
            val classCode = UUID.randomUUID().toString().substring(0 until 8)

            // BottomSheetDataReceiver 인터페이스를 통해 데이터를 전달 받음
            val lectureList = classManager.getInputClassData(classCode)

            lectureVM.addLecture(lectureList)
        }
    }

    /** 시간 및 장소 레이아웃 추가 버튼 이벤트 메소드 */
    private fun addEventClassInfoBtn(container: ViewGroup?){
        bind.addClassInfoBtn.setOnClickListener{
            classManager.addClassInfoLayout(container)
        }
    }
}