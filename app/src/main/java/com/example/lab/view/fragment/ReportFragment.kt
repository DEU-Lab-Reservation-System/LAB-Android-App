package com.example.lab.view.fragment
import android.app.AlertDialog

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.lab.R
import com.example.lab.application.MyApplication
import com.example.lab.data.requestDto.ReportRequestDto
import com.example.lab.databinding.FragmentReportBinding
import com.example.lab.view.activity.MainActivity
import com.example.lab.viewmodel.ReportViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReportFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // VARIABLE
    private lateinit var bind:FragmentReportBinding
    private lateinit var reportVM: ReportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_report, container, false)
        reportVM = ViewModelProvider(this)[ReportViewModel::class.java]

        activity?.let {
            (it as MainActivity).apply {
                hideTitleBar()
                hideBottomNavBar()
                // EditText 눌러도 UI 안 올라 오게
                window.attributes.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
            }
        }

        addClickEvent()
        return bind.root
    }

    private fun addClickEvent(){
        // 문의하기 버튼 클릭 이벤트
        bind.completeBtn.setOnClickListener {
            val data = MyApplication.member?.let { member ->
                ReportRequestDto.Send(
                    userId = member.userId,
                    userName = member.name,
                    title = "${bind.titleTv.editText?.text}",
                    content = "${bind.contentTv.editText?.text}"
                    )
            }
            
            data?.let { reportVM.sendReport(it) }
        }

        reportVM.reportFlag.observe(viewLifecycleOwner){ flag ->
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    if(flag.contentIfNotHandled() == true){
                        setTitle("문의 접수 완료")
                        setMessage("문의 접수가 완료되었습니다.")
                        setPositiveButton("확인") { dialog, _ -> dialog.dismiss()}
                    } else {
                        setTitle("문의 접수 오류")
                        setMessage(reportVM.reportError?:"오류가 발생했습니다.")
                        setPositiveButton("확인") { dialog, _ -> dialog.dismiss()}
                    }
                }
                builder.create()
            }
            alertDialog?.show()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        /**
         * 뒤로가기 버튼 콜백
         */
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.beginTransaction().remove(this@ReportFragment).commit();
                requireActivity().supportFragmentManager.popBackStack();
            }
        })
    }


    /**
     * 뷰 사라질 때 바텀 네비게이션 바 보이도록
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()
        activity?.let {
            (it as MainActivity).apply {
                showTitleBar()
                showBottomNavBar()
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
         * @return A new instance of fragment ReportFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}