package com.example.lab.view.bottomsheet

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.lab.R
import com.example.lab.custom.timetableview.Schedule
import com.example.lab.databinding.BottomsheetClassInfoBinding
import com.example.lab.utils.DateManager
import com.example.lab.viewmodel.LectureViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONObject
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClassInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@RequiresApi(Build.VERSION_CODES.O)
class ClassInfoFragment : BottomSheetDialogFragment() {

    //VARIABLE
    private lateinit var bind: BottomsheetClassInfoBinding
    private lateinit var lectureVm: LectureViewModel
    private lateinit var schedules: ArrayList<Schedule>

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_class_info, container, false)
        lectureVm = ViewModelProvider(requireActivity())[LectureViewModel::class.java]

        // 이전 프래그먼트에서 넘어온 Bundle 데이터가 있는 경우에만 실행
        arguments?.getString("classInfoJson")?.let {
            Log.i("수업 JSON", it)
            schedules = Schedule.toScheduleList(JSONObject(it))

            setClassInfo()
            addEditBtnEvent(it)
            addDeleteBtnEvent()
        }

        return bind.root
    }

    /**
     * 전달받은 시간표 정보 JSON을 TextView에 표시하는 메소드
     */
    private fun setClassInfo(){
        val classPlaceInfo = StringJoiner(", ")
        val classTimeInfo = StringBuilder()

        schedules.forEach{
            classTimeInfo.append(
                String.format(" %s %02d:%02d~%02d:%02d",
                    DateManager.day(it.day),
                    it.startTime.hour,
                    it.startTime.minute,
                    it.endTime.hour,
                    it.endTime.minute
                ))
            classPlaceInfo.add("정보공학관 ${it.classPlace}")
        }
        
        // 텍스트뷰에 데이터 세팅
        bind.classTitleTextView.text = schedules[0].classTitle
        bind.professorTextView.text = schedules[0].professorName
        bind.classTimeTextView.text = classTimeInfo
        bind.classPlaceTextView.text = classPlaceInfo.toString()
    }

    /**
     * 수정 버튼 클릭 이벤트 등록
     */
    private fun addEditBtnEvent(classInfoJson: String){
        bind.editBtnLayout.setOnClickListener {

            val bottomSheet = EditClassFragment()
            bottomSheet.arguments = Bundle().apply {
                putString("classInfoJson", classInfoJson)
            }

            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }
    }

    /**
     * 삭제 버튼 클릭 이벤트 등록
     */
    private fun addDeleteBtnEvent(){
        bind.deleteBtnLayout.setOnClickListener{
            // AlertDialog로 물어본 후 삭제 진행
            AlertDialog.Builder(requireContext())
                .setTitle("시간표 삭제")
                .setMessage("해당 수업을 삭제하시겠습니까 ?")
                .setPositiveButton("확인") { dialog, which ->
                    schedules[0].code?.let {
                        lectureVm.deleteLecture(it)
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("취소"){dialog, which -> dialog.dismiss()}
                .create()
                .show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val metrics = resources.displayMetrics

        // 바텀 시트의 최상위 Layout의 Height를 기기의 Height로 설정해줘야 위로 드래그 했을 때 FullScreen 가능
//        val layout = view.findViewById<View>(R.id.parentLayout)
//        layout.layoutParams.height = metrics.heightPixels
    }

}