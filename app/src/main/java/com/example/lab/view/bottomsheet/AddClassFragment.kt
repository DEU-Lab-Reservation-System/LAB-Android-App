package com.example.lab.view.bottomsheet

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab.R
import com.example.lab.databinding.BottomsheetManageClassBinding
import com.example.lab.view.viewmanager.ClassBottomSheetManager
import com.example.lab.viewmodel.LectureViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

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
            // 입력한 수업의 정보 받아오기
            val lectureList = classManager.getInputClassData(classCode)

            lectureVM.addLecture(lectureList)
        }

        lectureVM.addLectureFlag.observe(requireActivity()) {
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
        }
    }

    /** 시간 및 장소 레이아웃 추가 버튼 이벤트 메소드 */
    private fun addEventClassInfoBtn(container: ViewGroup?){
        bind.addClassInfoBtn.setOnClickListener{
            classManager.addClassInfoLayout(container)
        }
    }
}