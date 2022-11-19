package com.example.lab.view.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.lab.R
import com.example.lab.data.responseDto.ReportResponseDto
import com.example.lab.databinding.FragmentReportInfoBinding
import com.example.lab.utils.DateManager
import com.example.lab.utils.extension.*
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReportInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReportInfoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    // VARIABLE
    private lateinit var bind:FragmentReportInfoBinding
    private var report:ReportResponseDto.Report? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            report = it.getString("ReportJson")
                ?.let { it1 -> JSONObject(it1) }    // 넘어온 JSON이 있으면 JSONObject로 변환
                ?.let { it2 -> ReportResponseDto.Report.parseJson(it2) } // JSONObject로 변환 되었으면 Report로 변환
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_report_info, container, false)

        this.apply {
            hideTitleBar()
            hideNavBar()
        }

        initReportData()
        addBtnClickEvent()

        return bind.root
    }

    private fun addBtnClickEvent(){
        bind.completeBtn.setOnClickListener{
            this.backToPrevious()
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initReportData(){
        report?.let {
            bind.apply {
                explainText.text = "${it.writerName}(${it.userId}) 님께서 ${DateManager.getDateUntilDate(it.date)}일에 작성한 내용입니다."
                titleTv.editText?.setText(it.title)
                contentTv.editText?.setText(it.content)
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReportInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
