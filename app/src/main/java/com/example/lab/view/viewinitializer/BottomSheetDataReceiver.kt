package com.example.lab.view.viewinitializer

import java.util.ArrayList

/** BottomSheet의 데이터를 전달하기위한 인터페이스 */
interface BottomSheetDataReceiver {
    fun<T> setBottomSheetDatas(lectureList: ArrayList<T>):Boolean
}