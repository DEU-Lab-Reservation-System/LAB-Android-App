package com.example.lab.view.bottomsheet

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import com.example.lab.R
import com.example.lab.data.entity.Lecture
import com.example.lab.databinding.BottomsheetAddClassBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
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
    private lateinit var bind: BottomsheetAddClassBinding

    /** AddClassBottomSheet의 데이터를 전달하기위한 인터페이스 */
    interface BottomSheedDataReciever{
        fun setClassDatas(lectureList:ArrayList<Lecture>)
    }

    lateinit var dataReciever:BottomSheedDataReciever

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_add_class, container, false)

        addDatePicker()
        addEventClassInfoBtn(container)
        addCreateBtnEvent()

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
    private fun addCreateBtnEvent(){
        bind.addClassBtn.setOnClickListener {
            /**
             *  예외 처리 사항
             *  1. 입력이 안된 항목이 있는 경우
             *  2. 수업 시작 날짜가 끝 날짜보다 늦는 경우
             *  3. 시긴 및 장소 정보가 하나도 없는 경우
             *  4. 시간 및 장소가 중복되는 경우
             *  5. 수업 시작 시간이 수업 끝 시간보다 늦는 경우
             */

            var lectureList:ArrayList<Lecture> = arrayListOf()

            // UUID는 동일한 수업에 대해 한 번만 생성
            val classCode = UUID.randomUUID().toString().substring(0 until 8)
            
            // 수업 생성
            var lecture = Lecture(
                code = classCode,
                title = bind.titleEditText.text.toString(),
                professor = bind.professorEditText.text.toString(),
                startDate = bind.startDateEditText.text.toString(),
                endDate = bind.endDateEditText.text.toString(),
            )

            /** 수업 정보 추가 필드 Iterator */
            val iter:Iterator<View> = bind.classInfoLayout.children.iterator()

            iter.forEach { view ->
                // 추가 정보 필드 값만 바꿔서 list에 추가
                lectureList.add(
                    /** copy() 메소드 : lecture 클래스에서 파라미터로 받은 부분만 변경하고 나머지는 그대로 복사해줌 */
                    lecture.copy(
                        day = view.findViewById<Spinner>(R.id.daySelector).selectedItem.toString(),
                        startTime = view.findViewById<EditText>(R.id.startTimeEditText).text.toString(),
                        endTime = view.findViewById<EditText>(R.id.endTimeEditText).text.toString(),
                        place = view.findViewById<Spinner>(R.id.placeSelector).selectedItem.toString()
                    )
                )
            }

            // 인터페이스로 데이터 전달
            dataReciever.setClassDatas(lectureList)

            dismiss()
        }
    }

    /** 시간 및 장소 추가 버튼 이벤트 메소드 */
    private fun addEventClassInfoBtn(container: ViewGroup?){
        bind.addClassInfoBtn.setOnClickListener{
            addClassInfo(container)
        }
    }

    /** 시간 및 장소 추가 레이아웃 생성 메소드 */
    private fun addClassInfo(container: ViewGroup?){
        val classInfoLayout = bind.classInfoLayout

        // 뷰 생성
        var inflater = requireActivity().layoutInflater
        var classInfoView = inflater.inflate(R.layout.sub_add_classinfo, container, false)

        val daySelector = classInfoView.findViewById<Spinner>(R.id.daySelector)
        val placeTimeSelector = classInfoView.findViewById<Spinner>(R.id.placeSelector)

        // 스피너 어댑터 등록
        daySelector.adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.day_list,
            R.layout.spinner_custom_item
        ).also { adpater ->
            adpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        placeTimeSelector.adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.lab_fullname_list,
            R.layout.spinner_custom_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // 타입 피커 등록
        addTimePicker(classInfoView)
        
        classInfoLayout.addView(classInfoView)
    }

    /** TimePicker 등록  메소드 */
    private fun addTimePicker(classInfoView:View){
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR)

        val startTimeEditText = classInfoView.findViewById<EditText>(R.id.startTimeEditText)
        val endTimeEditText = classInfoView.findViewById<EditText>(R.id.endTimeEditText)

        startTimeEditText.setOnClickListener{
            val timePicker = TimePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, { timePicker, selectHour, selectMinute ->
                startTimeEditText.setText(String.format("%02d:%02d", selectHour, selectMinute))
            }, hour, 0, true)

            timePicker.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            timePicker.show()
        }

        endTimeEditText.setOnClickListener{
            val timePicker = TimePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, { timePicker, selectHour, selectMinute ->
                endTimeEditText.setText(String.format("%02d:%02d", selectHour, selectMinute))
            }, hour, 0, true)

            timePicker.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            timePicker.show()
        }
    }

    /** DatePicker 등록 메소드 */
    private fun addDatePicker(){
        var cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        bind.startDateEditText.setOnClickListener{

            val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, day ->
                bind.startDateEditText.setText(String.format("%d-%02d-%02d",year, month+1, day))
            }, year, month, day)

            datePickerDialog.show()
        }

        bind.endDateEditText.setOnClickListener{
            val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, day ->
                bind.endDateEditText.setText(String.format("%d-%02d-%02d",year, month+1, day))
            }, year, month, day)

            datePickerDialog.show()
        }
    }

}