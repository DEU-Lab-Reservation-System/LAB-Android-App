package com.example.lab.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.lab.R
import com.example.lab.application.MyApplication
import com.example.lab.data.responseDto.ReservResponseDto
import com.example.lab.utils.DateManager

class UnAuthReservAdapter(private val reservations:ReservResponseDto.ReservList) : RecyclerView.Adapter<UnAuthReservAdapter.ViewHolder>() {
    private val checkBoxStatus = SparseBooleanArray()

    inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val parentLayout : LinearLayout = itemView.findViewById(R.id.parentLayout)
        val checkbox     : CheckBox     = itemView.findViewById(R.id.checkbox)
        val studentTv    : TextView     = itemView.findViewById(R.id.studentTextView)
        val majorTv      : TextView     = itemView.findViewById(R.id.majorTextView)
        val seatNumTv    : TextView     = itemView.findViewById(R.id.seatNumberTextView)
        val reservTimeTv : TextView     = itemView.findViewById(R.id.reservTimeTextView)
    }

    // ViewHolder를 생성하는 메소드
    // ViewHolder는 위에서 Inner Class로 생성해줌
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sub_request_reserv, parent, false)

        return ViewHolder(view)
    }

    // ViewHolder에 데이터를 바인딩해주는 메소드
    // 생성자에서 전달받은 Reservations의 데이터를 Position에 맞게 ViewHolder에 할당
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reserv = reservations.reservList[position]

        holder.studentTv.text = "${reserv.name}(${reserv.userId})"
        holder.majorTv.text = reserv.major
        holder.seatNumTv.text = "${reserv.seatNum}번 좌석"
        holder.reservTimeTv.text = reserv.run {
            "${DateManager.dateParse(this.startTime)}-${DateManager.dateParse((this.endTime))}"
        }
        holder.checkbox.isChecked = checkBoxStatus[position] // 해당 포지션의 값으로 check 상태 기억

        holder.parentLayout.setOnClickListener{
            // 누를 때마다 체크 박스의 값을 반전 시킴
            checkBoxStatus.put(position, !holder.checkbox.isChecked)

            Toast.makeText(MyApplication.ApplicationContext(), "${position}번 째 클릭, ${holder.studentTv.text} ${checkBoxStatus[position]}", Toast.LENGTH_SHORT).show()
            notifyItemChanged(position) // 해당 포지션의 아이템에 변경이 있음을 알림
        }
    }

    override fun getItemCount(): Int {
        return reservations.reservList.size
    }

    fun getSelectedItem(): ArrayList<ReservResponseDto.Reserv> {
        val checkedItems = arrayListOf<ReservResponseDto.Reserv>()
        for (i in 0 .. itemCount){
            if(checkBoxStatus[i]){
                checkedItems.add(reservations.reservList[i])
            }
        }

        return checkedItems
    }
}
