package com.example.lab.view.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab.R
import com.example.lab.adapter.ReportListAdapter
import com.example.lab.adapter.UserListAdapter
import com.example.lab.databinding.FragmentReportListBinding
import com.example.lab.utils.DateManager
import com.example.lab.utils.extension.hideNavBar
import com.example.lab.utils.extension.hideTitleBar
import com.example.lab.utils.extension.showNavBar
import com.example.lab.utils.extension.showTitleBar
import com.example.lab.viewmodel.ReportViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReportListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReportListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // VARIABLE
    private lateinit var bind:FragmentReportListBinding
    private lateinit var reportVM:ReportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        reportVM = ViewModelProvider(this)[ReportViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_report_list, container, false)

        this.apply {
            hideTitleBar()
            hideNavBar()
        }

        initView()
        initReportData()

        return bind.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView(){
        bind.todayTV.text = DateManager.getTodayUntilDate()
    }

    /**
     * 리사이클러뷰에 Members 데이터 셋팅
     */
    private fun initReportData(){
        reportVM.getAllReport()

        // 리사이클러뷰에 데이터 세팅
        reportVM.reports.observe(viewLifecycleOwner){
            bind.reportListRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                // 어댑터 연결 & 클릭 이벤트 리스너 추가
                adapter = ReportListAdapter(it).apply {
                    setOnItemClickListener(object: ReportListAdapter.OnItemClickListner{
                        override fun onItemClick(view: View, position: Int) {
                            Log.i("${position}번 째 클릭", "${getItem(position)}")
//
//                            val memberManageFragment = MemberManageFragment()
//                            memberManageFragment.arguments = Bundle().apply {
//                                putString("MemberJson", "$member")
//                            }
//
//                            requireActivity().supportFragmentManager
//                                .beginTransaction()
//                                .replace(R.id.frameLayout, memberManageFragment)
//                                .addToBackStack(null)
//                                .commit()
                        }
                    })

                }

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()
        this.apply {
            showTitleBar()
            showNavBar()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReportListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}