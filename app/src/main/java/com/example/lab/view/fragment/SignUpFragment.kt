package com.example.lab.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.example.lab.R
import com.example.lab.databinding.FragmentSignUpBinding

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

    private var page = 0


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

        bind.nextBtn.isEnabled = false
        bind.nextBtn.setBackgroundColor(resources.getColor(R.color.gray))

        addIdCheckBtnEvent()
        addNextBtnEvent()

        return bind.root
    }

    /** 다음 버튼 이벤트 */
    private fun addNextBtnEvent(){
        bind.nextBtn.setOnClickListener{
            bind.viewFlipper.inAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.flipper_right_in)
            bind.viewFlipper.outAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.flipper_left_out)

            if(page < 2){
                bind.viewFlipper.showNext()
                page++

                if(page == 2) bind.nextBtn.text = "회원가입 완료"

                /** 다음으로 넘어갈 때 버튼 비활성화 (해당 페이지의 조건이 충족되어야 활성화 됨) */
                bind.nextBtn.isEnabled = false
                bind.nextBtn.setBackgroundColor(resources.getColor(R.color.gray))
            }
        }
    }

    /** 아이디 중복 확인 버튼 */
    private fun addIdCheckBtnEvent(){
        bind.idCheckBtn.setOnClickListener{
            /** 아이디 중복 체크 요청 보내서 true이면 다음 버튼 활성화 */
            bind.nextBtn.isEnabled = true
            bind.nextBtn.setBackgroundColor(resources.getColor(R.color.colorAccent))
        }
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