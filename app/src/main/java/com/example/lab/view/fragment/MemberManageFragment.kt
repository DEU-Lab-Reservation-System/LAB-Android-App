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
import com.example.lab.data.enum.Role
import com.example.lab.data.requestDto.MemberRequestDto
import com.example.lab.data.responseDto.MemberResponseDto
import com.example.lab.databinding.FragmentMemberManageBinding
import com.example.lab.utils.extension.hideNavBar
import com.example.lab.utils.extension.showNavBar
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
        addBtnClickEvent()

        return bind.root
    }

    private fun addBtnClickEvent(){
        bind.apply {
            // ?????? ?????? ?????? ??????
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

                    memberVM.updateMemberOfAdmin(updateMember)
                }
            }
            // ?????? ?????? ??????
            warningBtn.setOnClickListener {
                memberVM.warning("${numberEditText.editText?.text}")
            }
            
            // ????????? ??????
            resetBtn.setOnClickListener {
                memberVM.resetWarning("${numberEditText.editText?.text}")
            }
        }

        addObserver()
    }

    private fun addObserver(){
        // ?????? ?????? ?????? ?????????
        memberVM.apply {
            // ?????? ?????? ?????? ?????????
            updateUser.observe(viewLifecycleOwner) { result ->
                val alertDialog: AlertDialog? = activity?.let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        setTitle("?????? ?????? ?????? ??????")
                        setMessage("?????? ?????? ????????? ?????? ???????????????.")
                        setPositiveButton("??????") { dialog, _ ->
                            dialog.dismiss()
                            member = result
                            setProfileData() // ????????? ????????? ??????
                        }
                    }.create()
                }
                alertDialog?.show()
            }

            // ?????? ?????? ?????? ?????????
            updateFlag.observe(viewLifecycleOwner){ flag->
                val alertDialog: AlertDialog? = activity?.let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        if(flag.contentIfNotHandled() == false){
                            setTitle("?????? ?????? ?????? ??????")
                            setMessage(memberVM.updateError?:"????????? ??????????????????.")
                            setPositiveButton("??????") { dialog, _ -> dialog.dismiss()}
                        }
                    }.create()
                }
                alertDialog?.show()
            }
        }
        

        // ?????? ?????? ?????????
        memberVM.apply {
            // ?????? ?????? ?????????
            warningResult.observe(viewLifecycleOwner){ result ->
                val alertDialog: AlertDialog? = activity?.let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        setTitle("?????? ?????? ??????")
                        setMessage(result.message)
                        setPositiveButton("??????") { dialog, _ -> dialog.dismiss()}
                    }.create()
                }
                alertDialog?.show()
            }
            // ?????? ?????? ?????????
            warningError.observe(viewLifecycleOwner){ result ->
                val alertDialog: AlertDialog? = activity?.let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        setTitle("?????? ?????? ??????")
                        setMessage(result?:"????????? ??????????????????.")
                        setPositiveButton("??????") { dialog, _ -> dialog.dismiss()}
                    }.create()
                }
                alertDialog?.show()
            }
        }

        // ?????? ?????? ????????? ?????????
        memberVM.apply {
            // ?????? ?????? ????????? ?????????
            resetResult.observe(viewLifecycleOwner){ result ->
                val alertDialog: AlertDialog? = activity?.let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        setTitle("?????? ????????? ??????")
                        setMessage(result.message)
                        setPositiveButton("??????") { dialog, _ -> dialog.dismiss()}
                    }.create()
                }
                alertDialog?.show()
            }

            // ?????? ?????? ????????? ?????????
            resetError.observe(viewLifecycleOwner){ result ->
                val alertDialog: AlertDialog? = activity?.let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        setTitle("?????? ????????? ??????")
                        setMessage(result?:"????????? ??????????????????.")
                        setPositiveButton("??????") { dialog, _ -> dialog.dismiss()}
                    }.create()
                }
                alertDialog?.show()
            }
        }
    }


    /**
     * ????????? ????????? ?????? ?????????
     */
    private fun setProfileData(){
        bind.apply {
            member?.let {
                userIdTv.text   = it.userId
                userNameTv.text = it.name
                majorTv.text    = it.major
                userTypeTv.text = when(it.role){
                    Role.USER -> "?????????"
                    Role.USER_TAKEOFF -> "?????????"
                    Role.USER_GRADUATE -> "?????????"
                    Role.PROF -> "??????"
                    Role.ADMIN -> "??????"
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
     * ???????????? ????????? ??? ?????? ?????? ????????? ??????
     * (????????? ???????????? ???????????? ?????? ?????? ??????)
     */
    private fun setRoleRadioButton(role: Role){
        for (i in 0 until bind.roleRadioGroup.childCount) {
            val radioBtn = bind.roleRadioGroup.getChildAt(i) as RadioButton

            if (userRole(radioBtn.id).name == role.name) radioBtn.isChecked = true
            // ?????? ???????????? ?????? ????????? ??????
            // ex) USER_GRADUATE??? USER??? USER_TAKEOFF?????? ?????? ??????
            if (userRole(radioBtn.id).name.split("_")[0] != role.name.split("_")[0]) radioBtn.visibility = View.GONE
        }
    }

    /**
     * ????????? ????????? ?????? ?????? ????????? ???????????? ?????????
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
         * ???????????? ?????? ??????
         */
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.beginTransaction().remove(this@MemberManageFragment).commit();
                requireActivity().supportFragmentManager.popBackStack();
            }
        })
    }

    /**
     * ??? ????????? ??? ?????? ??????????????? ??? ????????????
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