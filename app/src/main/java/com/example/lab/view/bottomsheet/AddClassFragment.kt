package com.example.lab.view.bottomsheet

import android.app.ActionBar
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.CallSuper
import androidx.annotation.Nullable
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import com.example.lab.R
import com.example.lab.databinding.BottomsheetAddClassBinding
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
    private lateinit var bind: BottomsheetAddClassBinding

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_add_class, container, false)

        addDatePicker()
        addEventClassInfoBtn(container)

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val metrics = resources.displayMetrics

        // 바텀 시트의 최상위 Layout의 Height를 기기의 Height로 설정해줘야 위로 드래그 했을 때 FullScreen 가능
        val layout = view.findViewById<View>(R.id.parentLayout)
        layout.layoutParams.height = metrics.heightPixels
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
        val minute = cal.get(Calendar.MINUTE)

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
                bind.startDateEditText.setText("$year-${month+1}-${day}")
            }, year, month, day)

            datePickerDialog.show()
        }

        bind.endDateEditText.setOnClickListener{
            val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, day ->
                bind.endDateEditText.setText("$year-${month+1}-${day}")
            }, year, month, day)

            datePickerDialog.show()
        }
    }

}