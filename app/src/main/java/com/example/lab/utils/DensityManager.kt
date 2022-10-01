package com.example.lab.utils

import com.example.lab.application.MyApplication

object DensityManager {
    /** Dp를 Px로 변환해주는 메소드 */
    fun convertDPtoPX(dp:Int):Int {
        return MyApplication.ApplicationContext()?.let {
            (dp * it.resources.displayMetrics.density).toInt()
        }!!
    }
}