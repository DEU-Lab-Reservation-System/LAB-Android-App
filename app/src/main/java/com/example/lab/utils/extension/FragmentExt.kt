package com.example.lab.utils.extension
import android.app.AlertDialog
import android.content.DialogInterface

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
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