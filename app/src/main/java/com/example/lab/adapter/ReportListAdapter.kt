package com.example.lab.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.lab.R
import com.example.lab.data.responseDto.ReportResponseDto
import com.example.lab.utils.DateManager

class ReportListAdapter(private var reports: ReportResponseDto.Reports) : RecyclerView.Adapter<ReportListAdapter.ViewHolder>() {
    interface OnItemClickListner{
        fun onItemClick(view: View, position: Int)
    }
    private var mClickListener: OnItemClickListner?= null
    fun setOnItemClickListener(listner: OnItemClickListner){
        this.mClickListener = listner
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val parentLayout: LinearLayout = itemView.findViewById(R.id.parentLayout)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)
        val studentTv: TextView = itemView.findViewById(R.id.studentTextView)
        val majorTv: TextView = itemView.findViewById(R.id.majorTextView)
        val seatNumTv: TextView = itemView.findViewById(R.id.seatNumberTextView)
        val reservTimeTv: TextView = itemView.findViewById(R.id.reservTimeTextView)
    }

    // ViewHolder를 생성하는 메소드
    // ViewHolder는 위에서 Inner Class로 생성해줌
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.sub_request_reserv, parent, false)

        return ViewHolder(view)
    }

    // ViewHolder에 데이터를 바인딩해주는 메소드
    // 생성자에서 전달받은 Reservations의 데이터를 Position에 맞게 ViewHolder에 할당
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val report = reports.reportList[position]

        holder.checkbox.visibility     = View.GONE // 체크박스 숨기기
        holder.seatNumTv.visibility    = View.GONE // 좌석 텍스트뷰 숨기기

        holder.studentTv.text = "${report.writerName}(${report.userId})"
        holder.majorTv.text = report.content    // 문의 조회에선 majorTv 자리에 Content가 들어감
        holder.reservTimeTv.text = DateManager.getDateUntilDate(report.date) // 문의 조회에선 reservTime 자리에 작성일자가 들어감

        holder.parentLayout.setOnClickListener {
            mClickListener?.onItemClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return reports.reportList.size
    }

    fun getItem(pos:Int): ReportResponseDto.Report{
        return reports.reportList[pos]
    }
}