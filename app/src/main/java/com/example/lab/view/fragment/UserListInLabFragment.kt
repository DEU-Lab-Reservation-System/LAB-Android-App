package com.example.lab.view.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab.R
import com.example.lab.adapter.UserListInLabAdapter
import com.example.lab.data.requestDto.ReservRequestDto
import com.example.lab.databinding.FragmentUserListInLabBinding
import com.example.lab.view.activity.MainActivity
import com.example.lab.viewmodel.ReservViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserListInLabFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserListInLabFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // VARIABLE
    private lateinit var bind:FragmentUserListInLabBinding
    private lateinit var reservVM:ReservViewModel
    private var labInfo: ReservRequestDto.LabInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            labInfo = ReservRequestDto.LabInfo(
                startTime = it.getString("startTime") ?: "",
                endTime = it.getString("endTime") ?: "",
                roomNum = it.getString("roomNum") ?: "",
            )
        }

        reservVM = ViewModelProvider(this)[ReservViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_user_list_in_lab, container, false)

        activity?.let {
            (it as MainActivity).apply {
                hideTitleBar()
                hideBottomNavBar()
            }
        }

        initRecyclerView()
        addBackBtnClickEvent()

        return bind.root
    }

    /**
     * 리사이클러뷰 초기화
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun initRecyclerView(){
        labInfo?.let {
            reservVM.getUserListInLab(it)

            reservVM.userListInLab.observe(viewLifecycleOwner){
                bind.userListRecyclerView.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = UserListInLabAdapter(it.reservList)
                }
            }
        }
    }

    /**
     * 뒤로가기 버튼 이벤트
     */
    private fun addBackBtnClickEvent(){
        bind.backBtn.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .remove(this@UserListInLabFragment)
                .commit();
            requireActivity().supportFragmentManager.popBackStack();

        }
    }

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
         * @return A new instance of fragment UserListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserListInLabFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}