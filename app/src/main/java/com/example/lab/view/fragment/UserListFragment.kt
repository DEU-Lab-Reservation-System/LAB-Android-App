package com.example.lab.view.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab.R
import com.example.lab.adapter.UserListAdapter
import com.example.lab.adapter.UserListInLabAdapter
import com.example.lab.application.MyApplication
import com.example.lab.databinding.FragmentUserListBinding
import com.example.lab.utils.DateManager
import com.example.lab.utils.extension.hideNavBar
import com.example.lab.utils.extension.hideTitleBar
import com.example.lab.utils.extension.showNavBar
import com.example.lab.utils.extension.showTitleBar
import com.example.lab.view.activity.MainActivity
import com.example.lab.viewmodel.MemberViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // VARIABLE
    private lateinit var bind:FragmentUserListBinding
    private lateinit var memberVM:MemberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        // Fragment에서 띄워진 Fragment이므로 owner는 this로 줌
        // (requireActivity()로 주면 이 Fragment가 죽어도 뷰 모델은 살아있어서 옵저버 중복 등록됨)
        memberVM = ViewModelProvider(this)[MemberViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_user_list, container, false)
        bind.lifecycleOwner = viewLifecycleOwner

        this.apply {
            hideTitleBar()
            hideNavBar()
        }

        initView()
        initMembersData()
        addClickEventBackBtn()

        return bind.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView(){
        bind.todayTV.text = DateManager.getTodayUntilDate()
    }

    /**
     * 리사이클러뷰에 Members 데이터 셋팅
     */
    private fun initMembersData(){
        memberVM.getAllMembers()

        // 리사이클러뷰에 데이터 세팅
        memberVM.memberList.observe(viewLifecycleOwner){
            bind.userListRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                // 어댑터 연결 & 클릭 이벤트 리스너 추가
                adapter = UserListAdapter(it).apply {
                    setOnItemClickListener(object: UserListAdapter.OnItemClickListner{
                        override fun onItemClick(view: View, position: Int) {
                            Log.i("${position}번 째 클릭", "${getItem(position)}")
                            val member = getItem(position).toJson()
                            Log.d("Member Json", member.toString())

                            val memberManageFragment = MemberManageFragment()
                            memberManageFragment.arguments = Bundle().apply {
                                putString("MemberJson", "$member")
                            }

                            requireActivity().supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.frameLayout, memberManageFragment)
                                .addToBackStack(null)
                                .commit()
                        }
                    })

                }

            }
        }
    }

    /**
     * 뒤로가기 버튼 이벤트
     */
    private fun addClickEventBackBtn(){
        bind.backBtn.setOnClickListener{
            requireActivity().supportFragmentManager
                .beginTransaction()
                .remove(this@UserListFragment)
                .commit()
            requireActivity().supportFragmentManager.popBackStack()
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
         * @return A new instance of fragment UserListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}