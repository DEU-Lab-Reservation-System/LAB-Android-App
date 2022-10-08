package com.example.lab.view.bottomsheet

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import com.example.lab.R
import com.example.lab.custom.timetableview.Schedule
import com.example.lab.data.entity.Lecture
import com.example.lab.databinding.BottomsheetAddClassBinding
import com.example.lab.utils.DateManager
import com.github.tlaabs.timetableview.Time
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONObject
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

    @RequiresApi(Build.VERSION_CODES.O)
    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_add_class, container, false)

        addDatePicker()
        addEventClassInfoBtn(container)

        // 전달 받은 데이터가 없으면 시간표 추가 모드
        if(arguments?.isEmpty == true){
            addCreateBtnEvent()
        } else { // 전달 받은 데이터가 있으면 시간표 수정 모드
            arguments?.getString("classInfoJson")?.let { it ->
                val schedules = Schedule.toScheduleList(JSONObject(it))

                schedules[0].let {
                    setEditLayout(it.classTitle, it.professorName, "it.startTime", "it.endTime")
                }
                // 전달 받은 수업 정보를 레이아웃에 추가
                schedules.forEach { schedule ->
                    addClassInfoLayout(
                        container,
                        DateManager.day(schedule.day),
                        "정보공학관 ${schedule.classPlace}",
                        schedule.startTime,
                        schedule.endTime
                    )
                }
            }
        }

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val metrics = resources.displayMetrics

        // 바텀 시트의 최상위 Layout의 Height를 기기의 Height로 설정해줘야 위로 드래그 했을 때 FullScreen 가능
        val layout = view.findViewById<View>(R.id.parentLayout)
        layout.layoutParams.height = metrics.heightPixels
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
        bind.titleEditText.setText(title)
        bind.professorEditText.setText(professor)
        bind.startDateEditText.setText(startDate)
        bind.endDateEditText.setText(endDate)
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
            addClassInfoLayout(container)
        }
    }

    /**
     * @param container: ViewGroup?
     * @param day: String
     * @param place: String
     * @param startTime: Time (default : 현재 시간)
     * @param endTime: Time (default : 현재 시간)
     * 
     * 시간 및 장소 추가 레이아웃 생성 메소드
     * 수정 모드 일 경우 기존 수업 데이터가 채워진 채로 생성 됨
     */
    private fun addClassInfoLayout(
        container: ViewGroup?,
        day:String = "",
        place:String = "",
        startTime: Time = Time(Calendar.getInstance().get(Calendar.HOUR), 0),
        endTime: Time = Time(Calendar.getInstance().get(Calendar.HOUR), 0),
    ){
        val classInfoLayout = bind.classInfoLayout

        // 뷰 생성
        var inflater = requireActivity().layoutInflater
        var classInfoView = inflater.inflate(R.layout.sub_add_classinfo, container, false)

        // 스피너 생성
        val daySelector = classInfoView.findViewById<Spinner>(R.id.daySelector)
        val placeSelector = classInfoView.findViewById<Spinner>(R.id.placeSelector)

        // 스피너 어댑터 등록
        daySelector.adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.day_list,
            R.layout.spinner_custom_item
        ).also { adpater ->
            adpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        placeSelector.adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.lab_fullname_list,
            R.layout.spinner_custom_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // 스피너 기본 값 설정
        daySelector.setSelection(getSpinnerItemIndex(daySelector, "${day}요일"))
        placeSelector.setSelection(getSpinnerItemIndex(placeSelector, place))

        // 타입 피커 등록
        addTimePicker(classInfoView, startTime, endTime)
        
        classInfoLayout.addView(classInfoView)
    }

    /**
     * @param classInfoView: View
     * @param startTime: Time (default = 현재 시간)
     * @param endTime: Time (default = 현재 시간)
     *  TimePicker 등록  메소드
     */
    private fun addTimePicker(
        classInfoView:View,
        startTime:Time,
        endTime:Time,
    ){

        val startTimeEditText = classInfoView.findViewById<EditText>(R.id.startTimeEditText)
        val endTimeEditText = classInfoView.findViewById<EditText>(R.id.endTimeEditText)

        // 수업 시간 EditText 기본 값 설정
        startTimeEditText.setText(String.format("%02d:%02d", startTime.hour, startTime.minute))
        endTimeEditText.setText(String.format("%02d:%02d", endTime.hour, endTime.minute))

        addTimePickerToEditText(startTimeEditText, startTime)
        addTimePickerToEditText(endTimeEditText, endTime)
//        startTimeEditText.setOnClickListener{
//            val timePicker = TimePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, { timePicker, selectHour, selectMinute ->
//                startTimeEditText.setText(String.format("%02d:%02d", selectHour, selectMinute))
//            }, startTime.hour, startTime.minute, true)
//
//            timePicker.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            timePicker.show()
//        }
//
//        endTimeEditText.setOnClickListener{
//            val timePicker = TimePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, { timePicker, selectHour, selectMinute ->
//                endTimeEditText.setText(String.format("%02d:%02d", selectHour, selectMinute))
//            }, endTime.hour, endTime.minute, true)
//
//            timePicker.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            timePicker.show()
//        }
    }

    /**
     * EditText에 TimePicker 등록하는 메소드
     */
    private fun addTimePickerToEditText(editText:EditText, initTime:Time){
        editText.setOnClickListener{
            val timePicker = TimePickerDialog(requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, { timePicker, selectHour, selectMinute ->
                editText.setText(String.format("%02d:%02d", selectHour, selectMinute))
            }, initTime.hour, initTime.minute, true)

            timePicker.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            timePicker.show()
        }
    }

    /**
     * DatePicker 등록 메소드
     */
    private fun addDatePicker(){
        var cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        addDatePickerToEditText(bind.startDateEditText, year, month, day)
        addDatePickerToEditText(bind.endDateEditText, year, month, day)

//        bind.startDateEditText.setOnClickListener{
//            val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, day ->
//                bind.startDateEditText.setText(String.format("%d-%02d-%02d",year, month+1, day))
//            }, year, month, day)
//
//            datePickerDialog.show()
//        }
//
//        bind.endDateEditText.setOnClickListener{
//            val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, day ->
//                bind.endDateEditText.setText(String.format("%d-%02d-%02d",year, month+1, day))
//            }, year, month, day)
//
//            datePickerDialog.show()
//        }
    }

    /**
     * EditText에 DatePicker 등록하는 메소드
     */
    private fun addDatePickerToEditText(editText: EditText, year:Int, month:Int, day:Int){
        editText.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, day ->
                bind.endDateEditText.setText(String.format("%d-%02d-%02d", year, month + 1, day))
            }, year, month, day)

            datePickerDialog.show()
        }
    }
    /**
     * 스피너에서 item의 인덱스를 찾아주는 메소드
     */
    private fun getSpinnerItemIndex(spinner:Spinner, item:String): Int{
        for (i in 0 until spinner.count){
            if(spinner.getItemAtPosition(i).toString() == item) return i
        }
        
        return 0;
    }
}