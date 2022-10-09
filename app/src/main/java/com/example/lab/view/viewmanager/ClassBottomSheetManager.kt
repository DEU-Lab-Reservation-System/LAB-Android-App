package com.example.lab.view.viewmanager

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.core.view.children
import com.example.lab.R
import com.example.lab.data.entity.Lecture
import com.example.lab.databinding.BottomsheetManageClassBinding
import com.github.tlaabs.timetableview.Time
import java.util.*

class ClassBottomSheetManager(
    private var bind:BottomsheetManageClassBinding,
    private var inflater: LayoutInflater,
    private var context: Context
) {

    lateinit var dataReciever:BottomSheetDataReceiver

    /** 확인 버튼 이벤트 */
    fun getInputClassData(classCode:String): ArrayList<Lecture> {
            /**
             *  예외 처리 사항
             *  1. 입력이 안된 항목이 있는 경우
             *  2. 수업 시작 날짜가 끝 날짜보다 늦는 경우
             *  3. 시긴 및 장소 정보가 하나도 없는 경우
             *  4. 시간 및 장소가 중복되는 경우
             *  5. 수업 시작 시간이 수업 끝 시간보다 늦는 경우
             */
            // UUID는 동일한 수업에 대해 한 번만 생성
            var lectureList: ArrayList<Lecture> = arrayListOf()

        // 수업 생성
        var lecture = Lecture(
            code = classCode,
            title = bind.titleEditText.text.toString(),
            professor = bind.professorEditText.text.toString(),
            startDate = bind.startDateEditText.text.toString(),
            endDate = bind.endDateEditText.text.toString(),
        )

        /** 수업 정보 추가 필드 Iterator */
        val iter: Iterator<View> = bind.classInfoLayout.children.iterator()

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
        return lectureList
//
//            val result = dataReciever.setBottomSheetDatas(lectureList)
//
//            Log.i("인터페이스 반환 값", result.toString())
//            //dismiss()
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

    fun addClassInfoLayout(
        container: ViewGroup?,
        day:String = "",
        place:String = "",
        startTime: Time = Time(Calendar.getInstance().get(Calendar.HOUR), 0),
        endTime: Time = Time(Calendar.getInstance().get(Calendar.HOUR), 0),
    ){
        val classInfoLayout = bind.classInfoLayout

        // 뷰 생성
        var classInfoView = inflater.inflate(R.layout.sub_add_classinfo, container, false)

        // 스피너 생성
        val daySelector = classInfoView.findViewById<Spinner>(R.id.daySelector)
        val placeSelector = classInfoView.findViewById<Spinner>(R.id.placeSelector)

        /**
         * 스피너 어댑터 생성 메소드
         */
        fun createArrayAdapter(context: Context, textArrayResId:Int, textViewResId:Int): SpinnerAdapter{
            return ArrayAdapter.createFromResource(
                context, textArrayResId, textViewResId
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        }

        // 스피너 어댑터 등록
        daySelector.adapter = createArrayAdapter(context, R.array.day_list, R.layout.spinner_custom_item)
        placeSelector.adapter = createArrayAdapter(context, R.array.lab_fullname_list, R.layout.spinner_custom_item)

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
        classInfoView: View,
        startTime: Time,
        endTime: Time,
    ){
        val startTimeEditText = classInfoView.findViewById<EditText>(R.id.startTimeEditText)
        val endTimeEditText = classInfoView.findViewById<EditText>(R.id.endTimeEditText)

        // 수업 시간 EditText 기본 값 설정
        startTimeEditText.setText(String.format("%02d:%02d", startTime.hour, startTime.minute))
        endTimeEditText.setText(String.format("%02d:%02d", endTime.hour, endTime.minute))


        /**
         * EditText에 TimePicker 등록하는 메소드
         */
        fun addTimePickerToEditText(editText: EditText, initTime: Time){
            editText.setOnClickListener{
                val timePicker = TimePickerDialog(
                    context,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar, { timePicker, selectHour, selectMinute ->
                    editText.setText(String.format("%02d:%02d", selectHour, selectMinute))
                }, initTime.hour, initTime.minute, true)

                timePicker.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                timePicker.show()
            }
        }

        addTimePickerToEditText(startTimeEditText, startTime)
        addTimePickerToEditText(endTimeEditText, endTime)
    }



    /**
     * DatePicker 등록 메소드
     */
    fun addDatePicker(){
        var cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        /**
         * EditText에 DatePicker 등록하는 메소드 (확장 함수)
         */
        fun addDatePickerToEditText(editText: EditText, year:Int, month:Int, day:Int){
            editText.setOnClickListener {
                val datePickerDialog = DatePickerDialog(context, { _, year, month, day ->
                    editText.setText(String.format("%d-%02d-%02d", year, month + 1, day))
                }, year, month, day)

                datePickerDialog.show()
            }
        }

        addDatePickerToEditText(bind.startDateEditText, year, month, day)
        addDatePickerToEditText(bind.endDateEditText, year, month, day)
    }

    /**
     * 스피너에서 item의 인덱스를 찾아주는 메소드
     */
    private fun getSpinnerItemIndex(spinner: Spinner, item:String): Int{
        for (i in 0 until spinner.count){
            if(spinner.getItemAtPosition(i).toString() == item) return i
        }

        return 0;
    }
}