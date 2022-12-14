package com.example.lab.view.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab.R
import com.example.lab.application.MyApplication
import com.example.lab.data.enum.Role
import com.example.lab.data.requestDto.MemberRequestDto
import com.example.lab.databinding.FragmentEditProfileBinding
import com.example.lab.utils.extension.hideNavBar
import com.example.lab.utils.extension.hideTitleBar
import com.example.lab.utils.extension.showNavBar
import com.example.lab.utils.extension.showTitleBar
import com.example.lab.viewmodel.MemberViewModel
import com.google.android.material.textfield.TextInputLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var memberJson: String? = null

    // VARIABLE
    private lateinit var bind: FragmentEditProfileBinding
    private lateinit var memberVM: MemberViewModel
    private lateinit var editTextList:ArrayList<TextInputLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            memberJson = it.getString("MemberJson")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.hideTitleBar()
        this.hideNavBar()
        /**
         * ???????????? ?????? ??????
         */
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.beginTransaction().remove(this@EditProfileFragment).commit();
                requireActivity().supportFragmentManager.popBackStack();
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)
        memberVM = ViewModelProvider(this)[MemberViewModel::class.java]

        editTextList = arrayListOf(
            bind.phoneEditText,
            bind.emailEditText
        )

        setProfileData()
        addCompleteBtnEvent()
        addTextWatcher()

        return bind.root
    }

    private fun addTextWatcher(){
        bind.phoneEditText.editText?.addTextChangedListener(phoneTextWatcher)
        bind.emailEditText.editText?.addTextChangedListener(emailTextWatcher)
    }

    private fun addCompleteBtnEvent(){
        bind.apply {
            completeBtn.setOnClickListener {
                MyApplication.member?.let {
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
        // ?????? ?????? ?????? ?????????
        memberVM.updateFlag.observe(viewLifecycleOwner){ flag->
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    if(flag.contentIfNotHandled() == true){
                        setTitle("?????? ?????? ?????? ??????")
                        setMessage("?????? ?????? ????????? ?????? ???????????????.")
                        setPositiveButton("??????") { dialog, _ ->
                            dialog.dismiss()
                            setProfileData() // ????????? ????????? ??????
                        }
                    } else {
                        setTitle("?????? ?????? ?????? ??????")
                        setMessage(memberVM.updateError?:"????????? ??????????????????.")
                        setPositiveButton("??????") { dialog, _ -> dialog.dismiss()}
                    }
                }
                builder.create()
            }
            alertDialog?.show()
        }
    }
    /**
     * ????????? ????????? ?????? ?????????
     */
    private fun setProfileData(){
        bind.apply {
            MyApplication.member?.let {
                userNameTv.text = it.name
                userIdTv.text = it.userId
                majorTv.text = it.major
                Log.i("Role = ${Role.USER}", "????????? Role = ${it.role}")
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



    /**
     * ?????? ????????? ???????????? ????????? ???????????? ?????????
     */
    private fun isAllInputComplete(){
        editTextList.forEach {
            // ???????????? ?????? ????????????, ????????? ????????? ?????? ?????? ??????
            if(it.editText?.text.isNullOrEmpty() || it.isErrorEnabled){
                setCompleteBtnEnabled(false)
                return
            }
        }
        // ?????? EditText??? ????????? ?????????, ?????? ????????? ????????? ?????? ?????? ?????? ?????????
        setCompleteBtnEnabled(true)
    }

    /**
     * ?????? ?????? ?????? ?????? ?????????
     */
    private fun setCompleteBtnEnabled(status:Boolean){
        if(status){
            bind.completeBtn.isEnabled = true
            bind.completeBtn.setBackgroundColor(resources.getColor(R.color.colorAccent))
        } else {
            bind.completeBtn.isEnabled = false
            bind.completeBtn.setBackgroundColor(resources.getColor(R.color.gray))
        }
    }

    /**
     * ???????????? ?????? ?????? TextWatcher
     */
    private val phoneTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            val input = "$p0"

            bind.apply {
                if(input.matches(Regex("^\\d{3}-\\d{4}-\\d{4}\$"))){
                    phoneEditText.error = "- ?????? ??????????????????"
                    phoneEditText.isErrorEnabled = true
                }
                else if(!input.matches(Regex("^[0-9]{11}$"))) {
                    phoneEditText.error = "???????????? ?????? ???????????????."
                    phoneEditText.isErrorEnabled = true
                }
                else{
                    phoneEditText.error = null
                    phoneEditText.isErrorEnabled = false
                }
                isAllInputComplete()
            }
        }
    }

    /**
     * ????????? ?????? ?????? TextWatcher
     */
    private val emailTextWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            val pattern = Patterns.EMAIL_ADDRESS

            bind.apply {
                if(!pattern.matcher("$p0").matches()){
                    emailEditText.error = "???????????? ?????? ????????? ???????????????."
                    emailEditText.isErrorEnabled = true
                }
                else{
                    emailEditText.error = null
                    emailEditText.isErrorEnabled = false
                }
                isAllInputComplete()
            }
        }
    }

    /**
     * ??? ????????? ??? ?????? ??????????????? ??? ????????????
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()
        this.showTitleBar()
        this.showNavBar()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}