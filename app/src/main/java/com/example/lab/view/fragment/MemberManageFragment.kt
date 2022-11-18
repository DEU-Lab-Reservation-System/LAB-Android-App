package com.example.lab.view.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.lab.R
import com.example.lab.data.entity.Member
import com.example.lab.data.enum.Role
import com.example.lab.data.requestDto.MemberRequestDto
import com.example.lab.data.responseDto.MemberResponseDto
import com.example.lab.databinding.FragmentMemberManageBinding
import com.example.lab.utils.extension.hideNavBar
import com.example.lab.utils.extension.showNavBar
import com.example.lab.view.activity.MainActivity
import com.example.lab.viewmodel.MemberViewModel
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MemberManageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MemberManageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var bind:FragmentMemberManageBinding
    private lateinit var memberVM:MemberViewModel
    private var member: MemberResponseDto.Member?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            member = it.getString("MemberJson")
                ?.let { it1 -> JSONObject(it1) }
                ?.let { it2 -> MemberResponseDto.Member.parseJson(it2) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_member_manage, container, false)
        memberVM = ViewModelProvider(this)[MemberViewModel::class.java]

        this.hideNavBar()

        setProfileData()
        addCompleteBtnEvent()

        return bind.root
    }

    private fun addCompleteBtnEvent(){
        bind.apply {
            completeBtn.setOnClickListener {
                member?.let {
                    val updateMember = MemberRequestDto.Update.createDto(it)

                    updateMember.apply {
                        name = "${nameEditText.editText?.text}"
                        userId = "${numberEditText.editText?.text}"
                        major = "${majorEditText.editText?.text}"
                        phoneNumber = "${phoneEditText.editText?.text}"
                        email = "${emailEditText.editText?.text}"
                        role = userRole(bind.roleRadioGroup.checkedRadioButtonId)
                    }

                    memberVM.updateMember(updateMember)
                }
            }
        }

        addObserver()
    }

    private fun addObserver(){
        // 회원 정보 수정 성공시
        memberVM.updateFlag.observe(viewLifecycleOwner){ flag->
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    if(flag.contentIfNotHandled() == true){
                        setTitle("회원 정보 수정 완료")
                        setMessage("회원 정보 수정이 완료 되었습니다.")
                        setPositiveButton("확인") { dialog, _ ->
                            dialog.dismiss()
                            setProfileData() // 수정된 정보로 변경
                        }
                    } else {
                        setTitle("회원 정보 수정 실패")
                        setMessage(memberVM.updateError?:"오류가 발생했습니다.")
                        setPositiveButton("확인") { dialog, _ -> dialog.dismiss()}
                    }
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
            member?.let {
                userIdTv.text   = it.userId
                userNameTv.text = it.name
                majorTv.text    = it.major
                userTypeTv.text = when(it.role){
                    Role.USER -> "재학생"
                    Role.USER_TAKEOFF -> "휴학생"
                    Role.USER_GRADUATE -> "졸업생"
                    Role.PROF -> "교수"
                    Role.ADMIN -> "조교"
                    else -> "???"
                }
                nameEditText.editText?.setText(it.name)
                numberEditText.editText?.setText(it.userId)
                majorEditText.editText?.setText(it.major)
                phoneEditText.editText?.setText(it.phoneNumber)
                emailEditText.editText?.setText(it.email)

                setRoleRadioButton(it.role)
            }
        }
    }

    /**
     * 사용자가 변경할 수 있는 회원 유형만 표시
     * (학생이 관리자로 변경하는 것을 막기 위함)
     */
    private fun setRoleRadioButton(role: Role){
        for (i in 0 until bind.roleRadioGroup.childCount) {
            val radioBtn = bind.roleRadioGroup.getChildAt(i) as RadioButton

            if (userRole(radioBtn.id).name == role.name) radioBtn.isChecked = true
            // 변경 불가능한 회원 유형은 숨김
            // ex) USER_GRADUATE는 USER와 USER_TAKEOFF로만 변경 가능
            if (userRole(radioBtn.id).name.split("_")[0] != role.name.split("_")[0]) radioBtn.visibility = View.GONE
        }
    }

    /**
     * 라디오 버튼에 따라 회원 유형을 반환하는 메소드
     */
    private fun userRole(checkedId: Int): Role{
        return when(checkedId){
            R.id.student -> Role.USER
            R.id.takeoff -> Role.USER_TAKEOFF
            R.id.graduate -> Role.USER_GRADUATE
            R.id.prof -> Role.PROF
            R.id.admin -> Role.ADMIN
            else -> Role.USER
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        /**
         * 뒤로가기 버튼 콜백
         */
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.beginTransaction().remove(this@MemberManageFragment).commit();
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
        this.showNavBar()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MemberManageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MemberManageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}