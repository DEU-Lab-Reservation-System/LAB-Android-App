package com.example.lab.utils.extension
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.lab.application.MyApplication
import com.example.lab.view.activity.LoginActivity
import com.example.lab.view.activity.MainActivity

@RequiresApi(Build.VERSION_CODES.O)
fun Fragment.hideNavBar(){
    this.activity?.let {
        (it as MainActivity).apply {
            hideTitleBar()
            hideBottomNavBar()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Fragment.hideTitleBar(){
    this.activity?.let {
        (it as MainActivity).apply {
            hideTitleBar()
            hideBottomNavBar()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Fragment.showNavBar(){
    this.activity?.let {
        (it as MainActivity).apply {
            showBottomNavBar()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Fragment.showTitleBar(){
    this.activity?.let {
        (it as MainActivity).apply {
            showTitleBar()
        }
    }
}

/**
 * 이전 프래그먼트로 돌아가는 메소드
 */
fun Fragment.backToPrevious(){
    requireActivity().supportFragmentManager
        .beginTransaction()
        .remove(this)
        .commit()
    requireActivity().supportFragmentManager.popBackStack();
}

fun Fragment.dismiss(){
    requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
    requireActivity().supportFragmentManager.popBackStack()
}

fun Fragment.showOkAlert(title:String, content:String){
    val alertDialog: AlertDialog? = activity?.let {
        val builder = AlertDialog.Builder(it)
        builder.apply {
            setTitle(title)
            setMessage(content)
            setPositiveButton("확인") { dialog, _ -> dialog.dismiss()}
        }
        builder.create()
    }
    alertDialog?.show()
}

fun Fragment.showOkCancleAlert(title:String, content:String, operation: Unit){
    val alertDialog: AlertDialog? = activity?.let {
        val builder = AlertDialog.Builder(it)
        builder.apply {
            setTitle(title)
            setMessage(content)
            setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
                operation
            }
            setNegativeButton("취소"){dialog, _ -> dialog.dismiss()}
        }
        builder.create()
    }
    alertDialog?.show()
}

/**
 * 로그아웃시 사용되는 메소드
 */
fun Fragment.backToLogin(){
    activity?.let {
        val intent = Intent(it.applicationContext, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        /**
         * LoginActivity가 실행될 때 loginFlag 옵저버가 실행 됨
         * 그 때 MyApplication에 Member 데이터가 있으면 바로 가져가서 로그인해버림
         * 로그아웃 할 때 null로 설정해서 자동 로그인되는 것을 방지
         */
        MyApplication.member = null

        startActivity(intent) //intent 에 명시된 액티비티로 이동
        it.finish() //현재 액티비티 종료
    }
}