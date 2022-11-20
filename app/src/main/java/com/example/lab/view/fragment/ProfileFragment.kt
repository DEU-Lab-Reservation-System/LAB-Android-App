package com.example.lab.view.fragment
import android.app.AlertDialog

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab.R
import com.example.lab.application.MyApplication
import com.example.lab.data.enum.Role
import com.example.lab.databinding.FragmentProfileBinding
import com.example.lab.utils.extension.backToLogin
import com.example.lab.view.activity.LoginActivity
import com.example.lab.view.activity.MainActivity
import com.example.lab.view.viewinitializer.ViewInitializer
import com.example.lab.view.viewinitializer.ViewInitializerFactory
import com.example.lab.viewmodel.MemberViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    // VARIABLE
    private lateinit var bind: FragmentProfileBinding
    private lateinit var memberVM: MemberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        memberVM = ViewModelProvider(requireActivity())[MemberViewModel::class.java]

        MyApplication.member?.let {
            ViewInitializerFactory().getInitializer(it.role, "PROFILE").init(this, bind)
        }

        setProfileData()
        addObserver()
        addMenuClickEvent()
        return bind.root
    }

    private fun addObserver(){
        // 회원 정보가 업데이트 되면 자동으로 갱신
        memberVM.updateFlag.observe(viewLifecycleOwner){
            setProfileData()
        }

        /**
         * 회원 탈퇴 옵저버
         */
        memberVM.withdrawResult.observe(viewLifecycleOwner){ result->
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setTitle("회원 탈퇴 완료")
                    setMessage(result.message)
                    setPositiveButton("확인") {
                        dialog, _ -> dialog.dismiss()
                        this@ProfileFragment.backToLogin()
                    }
                }
                builder.create()
            }
            alertDialog?.show()
        }
        
        memberVM.withdrawError.observe(viewLifecycleOwner){ result ->
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setTitle("회원 탈퇴 실패")
                    setMessage(result?:"오류가 발생하였습니다.")
                    setPositiveButton("확인") { dialog, _ -> dialog.dismiss()}
                }
                builder.create()
            }
            alertDialog?.show()
        }
    }

    /**
     * 프로필 데이터 세팅 메소드
     */
    private fun setProfileData(){
        bind.apply {
            MyApplication.member?.let {
                userNameTv.text = it.name
                userIdTv.text = it.userId
                majorTv.text = it.major
                Log.i("Role = ${Role.USER}", "사용자 Role = ${it.role}")
                userTypeTv.text = when(it.role){
                    Role.USER -> "재학생"
                    Role.USER_TAKEOFF -> "휴학생"
                    Role.USER_GRADUATE -> "졸업생"
                    Role.PROF -> "교수"
                    Role.ADMIN -> "조교"
                    else -> "???"
                }
            }
        }
    }

    private fun addMenuClickEvent(){
        // 프로필 수정 버튼 이벤트
        bind.profileMenuLayout.setOnClickListener{
            requireActivity().supportFragmentManager
                .beginTransaction()
                .add(R.id.frameLayout, EditProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        // 회원 탈퇴 버튼 클릭 이벤트
        bind.withdrawMenuLayout.setOnClickListener{
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setTitle("회원 탈퇴")
                    setMessage("탈퇴 하시겠습니까 ?")
                    setPositiveButton("확인") { dialog, _ ->
                        dialog.dismiss()
                        MyApplication.member?.let { memberVM.withdrawal(it.userId) }
                    }
                    setNegativeButton("취소") { dialog, _ -> dialog.dismiss() }
                }
                builder.create()
            }
            alertDialog?.show()
        }

        bind.reportMenuLayout.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .add(R.id.frameLayout, ReportFragment())
                .addToBackStack(null)
                .commit()
        }

        // 로그아웃 버튼 클릭 이벤트
        bind.logoutMenuLayout.setOnClickListener {
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setTitle("시스템 알림")
                    setMessage("로그아웃 하시겠습니까 ?")
                    setPositiveButton("확인") { dialog, _ ->
                        dialog.dismiss()
                        this@ProfileFragment.backToLogin()
                    }
                    setNegativeButton("취소") {dialog, _ -> dialog.dismiss()}
                }
                builder.create()
            }
            alertDialog?.show()
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}