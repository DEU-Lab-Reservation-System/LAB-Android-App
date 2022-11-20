package com.example.lab.view.viewinitializer.profile
import android.app.AlertDialog
import android.os.Build

import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lab.R
import com.example.lab.databinding.FragmentNotificationBinding
import com.example.lab.databinding.FragmentProfileBinding
import com.example.lab.view.fragment.NotificationFragment
import com.example.lab.view.fragment.ProfileFragment
import com.example.lab.view.fragment.ReportListFragment
import com.example.lab.view.fragment.UserListFragment
import com.example.lab.view.viewinitializer.ViewInitializer
import com.example.lab.viewmodel.TokenViewModel

class AdminProfileViewInitializer:ViewInitializer {
    private lateinit var fragment: ProfileFragment
    private lateinit var bind: FragmentProfileBinding
    private lateinit var tokenVM: TokenViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun init(fragment: Fragment, bind: ViewDataBinding) {
        this.fragment = fragment as ProfileFragment
        this.bind = bind as FragmentProfileBinding
        this.fragment.apply {
            tokenVM = ViewModelProvider(requireActivity())[TokenViewModel::class.java]
        }

        initView()
        addMenuClickEvent()
        addObserver()
    }

    private fun initView(){
        bind.apply {
            // 숨겨지는 레이아웃
            reservHistoryMenuLayout.visibility  = View.GONE
            reportMenuLayout.visibility         = View.GONE
            

            // 보여지는 레이아웃
            userListMenuLayout.visibility       = View.VISIBLE   // 회원 목록 조회
            reportListMenuLayout.visibility     = View.VISIBLE   // 불편사항 접수
            tokenMenuLayout.visibility          = View.VISIBLE   // 토큰 발급
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addMenuClickEvent(){
        bind.apply {
            // 회원 목록 조회 메뉴 이벤트
            userListMenuLayout.setOnClickListener {
                fragment.apply {
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .add(R.id.frameLayout, UserListFragment())
                        .commit()
                }
            }

            reportListMenuLayout.setOnClickListener{
                fragment.apply {
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .add(R.id.frameLayout, ReportListFragment())
                        .commit()
                }
            }
            // 토큰 발급 메뉴 이벤트
            tokenMenuLayout.setOnClickListener {
                fragment.apply {
                    val alertDialog: AlertDialog? = activity?.let {
                        val builder = AlertDialog.Builder(it)
                        builder.apply {
                            setTitle("토큰 발급")
                            setMessage("토큰을 발급하시겠습니까 ?")
                            setPositiveButton("확인") { dialog, _ ->
                                dialog.dismiss()
                                tokenVM.createToken()
                            }
                            setNegativeButton("취소") { dialog, _ -> dialog.dismiss() }
                        }
                        builder.create()
                    }
                    alertDialog?.show()
                }
            }
        }
    }

    private fun addObserver(){
        this.fragment.apply {
            tokenVM.token.observe(viewLifecycleOwner){ token ->
                val alertDialog: AlertDialog? = activity?.let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        setTitle("토큰 발급 완료")
                        setMessage("발급된 토큰은 $token 입니다.")
                        setPositiveButton("확인") { dialog, _ -> dialog.dismiss()}
                    }
                    builder.create()
                }
                alertDialog?.show()
            }

            tokenVM.createError.observe(viewLifecycleOwner){ error ->
                val alertDialog: AlertDialog? = activity?.let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        setTitle("토큰 발급 실패")
                        setMessage(error)
                        setPositiveButton("확인") { dialog, _ -> dialog.dismiss()}
                    }
                    builder.create()
                }
                alertDialog?.show()
            }
        }
    }
}