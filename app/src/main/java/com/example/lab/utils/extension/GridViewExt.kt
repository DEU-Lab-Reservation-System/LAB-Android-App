package com.example.lab.utils.extension

import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import androidx.core.view.size
import com.example.lab.R
import com.example.lab.adapter.SeatAdapter
import com.example.lab.adapter.data.SeatStatus

/**
 * 그리드뷰 확장 함수
 * 사용 중인 좌석을 표시하는 메소드
 */
fun GridView.markSeatInUser(seatlist:ArrayList<Int>, idx:Int){
    val seatNum = this.getItemAtPosition(idx) as SeatStatus

    // 그리드뷰가 초기화 되기 전에 옵저버가 호출될 수 있으므로 Empty 체크
    if(this.isNotEmpty() && this.size > idx){
        this[idx].findViewById<View>(R.id.seat).apply {
            background = if(seatlist.contains(seatNum.idx)) {
                seatNum.status = true
                resources.getDrawable(R.drawable.shape_seat_selected)
            }
            else {
                seatNum.status = false
                resources.getDrawable(R.drawable.shape_seat)
            }
        }
    }
}