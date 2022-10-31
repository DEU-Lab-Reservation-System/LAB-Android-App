package com.example.lab.view.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lab.R
import com.example.lab.application.MyApplication
import com.example.lab.data.enum.Role
import com.example.lab.data.requestDto.MemberRequestDto
import com.example.lab.databinding.FragmentSignUpBinding
import com.example.lab.viewmodel.MemberViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    // VARIABLE
    private lateinit var bind:FragmentSignUpBinding
    private lateinit var memberVM: MemberViewModel
    private lateinit var editTextList:ArrayList<TextInputLayout>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        memberVM = ViewModelProvider(requireActivity())[MemberViewModel::class.java]

        editTextList = arrayListOf(
            bind.nameEditText,
            bind.numberEditText,
            bind.majorEditText,
            bind.phoneEditText,
            bind.emailEditText
        )

        setNextBtnEnabled(false)
        addNextBtnEvent()
        addIdCheckBtnEvent()
        checkPassword()
        checkStudentInfo()

        return bind.root
    }

    /** 다음 버튼 이벤트 */
    private fun addNextBtnEvent(){
        bind.nextBtn.setOnClickListener{
            bind.viewFlipper.inAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.flipper_right_in)
            bind.viewFlipper.outAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.flipper_left_out)

            val display = bind.viewFlipper.displayedChild

            /** 다음으로 넘어갈 때 버튼 비활성화 (해당 페이지의 조건이 충족되어야 활성화 됨) */
            setNextBtnEnabled(false)

            when(display){
                0 -> bind.viewFlipper.showNext()
                1 -> {
                    bind.viewFlipper.showNext()
                    bind.nextBtn.text = "회원가입 완료"
                }
                2 -> signUp()
                3 ->{
                    requireActivity().supportFragmentManager.beginTransaction().remove(this@SignUpFragment).commit();
                    requireActivity().supportFragmentManager.popBackStack();
                }
            }
        }
    }

    /**
     * 회원가입 요청 메소드
     */
    private fun signUp(){
        memberVM.signUp(
            MemberRequestDto.SignUp(
                bind.idEditText.editText?.text.toString(),
                bind.pwEditText.editText?.text.toString(),
                bind.nameEditText.editText?.text.toString(),
                bind.emailEditText.editText?.text.toString(),
                bind.phoneEditText.editText?.text.toString(),
                userRole(bind.roleRadioGroup.checkedRadioButtonId)
            )
        )

        /** 회원가입 성공시 */
        memberVM.signUpFlag.observe(requireActivity()){
            // 회원 정보 입력 화면일때만 넘어가도록
            if(bind.viewFlipper.displayedChild == 2) {
                bind.viewFlipper.showNext()
                bind.nextBtn.text = "로그인으로 이동"
                setNextBtnEnabled(true)
            }
        }

        /** 회원가입 실패시 */
        memberVM.signUpError.observe(requireActivity()){
            AlertDialog.Builder(requireContext()).apply {
                setTitle("회원가입 실패")
                setMessage("회원가입에 실패 했습니다. 다시 시도해주세요.")
                setPositiveButton("OK"){ dialog, _ -> dialog.dismiss()}
                create()
                show()
            }
        }
    }

    /**
     * 라디오 버튼에 따라 회원 유형을 반환하는 메소드
     */
    private fun userRole(checkedId: Int): Role{
        return when(checkedId){
            R.id.student -> Role.USER
            R.id.takeoff -> Role.TAKEOFF
            R.id.graduate -> Role.GRADUATE
            R.id.admin -> Role.ADMIN
            else -> Role.USER
        }
    }

    /** 회원가입 1. 아이디 중복 확인 체크 */
    private fun addIdCheckBtnEvent(){
        bind.idEditText.editText?.addTextChangedListener(idCheckTextWatcher)

        /** 아이디 중복 체크 요청 보내서 true이면 다음 버튼 활성화 */
        bind.idCheckBtn.setOnClickListener{
            memberVM.idCheck("${bind.idEditText.editText?.text}")
        }

        /** 옵저버를 setOnClickListener 밖에 빼놔야 계속 동작함 */
        memberVM.idCheckFlag.observe(requireActivity()){
            // 중복체크 성공 시
            if(it){
                bind.idEditText.error = null
                bind.idEditText.helperText = "사용 가능한 아이디입니다."
                setNextBtnEnabled(true)
            }
        }

        // 로그인 실패시
        memberVM.idCheckError.observe(requireActivity()){
            bind.idEditText.error = it.contentIfNotHandled()
            setNextBtnEnabled(false)
        }
    }

    /**
     * 회원가입 2. 비밀번호 형식 & 확인 체크
     */
    private fun checkPassword(){
        bind.pwEditText.editText?.addTextChangedListener(pwTextWatcher)
        bind.pwConfirmEditText.editText?.addTextChangedListener(pwConfirmTextWatcher)
    }

    /**
     * 회원가입 3. 회원 정보 체크
     */
    private fun checkStudentInfo(){
        bind.numberEditText.editText?.addTextChangedListener(numberTextWatcher)
        bind.phoneEditText.editText?.addTextChangedListener(phoneTextWatcher)
        bind.emailEditText.editText?.addTextChangedListener(emailTextWatcher)
    }

    /**
     * 비밀번호 체크 TextWatcher
     */
    private val pwTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            // 영문과 숫자가 모두 포함되고 6자리 이상인지 체크
            if(!"$p0".matches(Regex("(?=.*[a-zA-Z])(?=.*[0-9]).{6,}"))){
                bind.pwEditText.error = "비밀번호는 영문과 숫자 포함 6자 이상이어야합니다."
                return
            }
            else bind.pwEditText.error = null
        }
    }

    /**
     * 아이디 변경 체크 TextWatcher
     */
    private val idCheckTextWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            // id 필드의 값이 변경되면 아이디 중복 확인을 무조건 다시하도록 설정
            setNextBtnEnabled(false)
        }
    }
    /**
     * 비밀번호 확인 TextWatcher
     */
    private val pwConfirmTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            // 영문과 숫자가 모두 포함되고 6자리 이상인지 체크
            if("${bind.pwEditText.editText?.text}" != "$p0"){
                bind.pwConfirmEditText.error = "비밀번호가 일치하지 않습니다."
                setNextBtnEnabled(false)
            }
            else{
                bind.pwConfirmEditText.error = null
                setNextBtnEnabled(true)
            }
        }
    }

    /**
     * 학번 형식 체크 TextWatcher
     */
    private val numberTextWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            if(!"$p0".matches(Regex("^[0-9]{8}$"))){
                bind.numberEditText.error = "학번은 8자리 숫자여야합니다."
                bind.numberEditText.isErrorEnabled = true
            }
            else{
                bind.numberEditText.error = null
                bind.numberEditText.isErrorEnabled = false
            }

            isAllInputComplete()
        }
    }


    /**
     * 전화번호 형식 체크 TextWatcher
     */
    private val phoneTextWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            val input = "$p0"

            if(input.matches(Regex("^\\d{3}-\\d{4}-\\d{4}\$"))){
                bind.phoneEditText.error = "- 없이 입력해주세요"
                bind.phoneEditText.isErrorEnabled = true
            }
            else if(!input.matches(Regex("^[0-9]{11}$"))) {
                bind.phoneEditText.error = "올바르지 않은 형식입니다."
                bind.phoneEditText.isErrorEnabled = true
            }
            else{
                bind.phoneEditText.error = null
                bind.phoneEditText.isErrorEnabled = false
            }

            isAllInputComplete()
        }
    }

    /**
     * 이메일 형식 체크 TextWatcher
     */
    private val emailTextWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            val pattern = Patterns.EMAIL_ADDRESS

            if(!pattern.matcher("$p0").matches()){
                bind.emailEditText.error = "올바르지 않은 이메일 형식입니다."
                bind.emailEditText.isErrorEnabled = true
            }
            else{
                bind.emailEditText.error = null
                bind.emailEditText.isErrorEnabled = false
            }

            isAllInputComplete()
        }
    }

    /**
     * 모든 입력이 올바르게 됐는지 체크하는 메소드
     */
    private fun isAllInputComplete(){
        editTextList.forEach {
            // 입력되지 않은 칸이거나, 형식이 잘못된 칸이 있는 경우
            if(it.editText?.text.isNullOrEmpty() || it.isErrorEnabled){
                setNextBtnEnabled(false)
                return
            }
        }
        // 모든 EditText가 입력이 되었고, 형식 체크도 완료된 경우 다음 버튼 활성화
        setNextBtnEnabled(true)
    }
    /**
     * 다음 버튼 상태 설정 메소드
     */
    private fun setNextBtnEnabled(status:Boolean){
        if(status){
            bind.nextBtn.isEnabled = true
            bind.nextBtn.setBackgroundColor(resources.getColor(R.color.colorAccent))
        } else {
            bind.nextBtn.isEnabled = false
            bind.nextBtn.setBackgroundColor(resources.getColor(R.color.gray))
        }
    }

    override fun onResume() {
        super.onResume()
        setNextBtnEnabled(false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignUpFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignUpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}