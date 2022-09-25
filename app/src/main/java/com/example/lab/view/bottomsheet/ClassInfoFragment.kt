package com.example.lab.view.bottomsheet

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import com.example.lab.R
import com.example.lab.databinding.BottomsheetClassInfoBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClassInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClassInfoFragment : BottomSheetDialogFragment() {

    //VARIABLE
    private lateinit var bind: BottomsheetClassInfoBinding

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bind = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_class_info, container, false)

        val mArgs = arguments

        bind.classTitleTextView.text = mArgs?.getString("classTitle")
        bind.professorTextView.text = mArgs?.getString("professor")
        bind.classTimeTextView.text = mArgs?.getString("classTime")
        bind.classPlaceTextView.text = mArgs?.getString("classPlace")

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val metrics = resources.displayMetrics

        // 바텀 시트의 최상위 Layout의 Height를 기기의 Height로 설정해줘야 위로 드래그 했을 때 FullScreen 가능
//        val layout = view.findViewById<View>(R.id.parentLayout)
//        layout.layoutParams.height = metrics.heightPixels
    }

}