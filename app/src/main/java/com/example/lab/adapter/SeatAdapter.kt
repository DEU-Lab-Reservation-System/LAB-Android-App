package com.example.lab.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.lab.R

class SeatAdapter(var context: Context,var seatList: List<Int>): BaseAdapter(){
    lateinit var inflater: LayoutInflater


    override fun getCount(): Int {
        return seatList.size
    }

    override fun getItem(position: Int): Any {
        return seatList[position]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        var view = view
        if(!::inflater.isInitialized){
            inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        if(view == null){
            view = inflater.inflate(R.layout.sub_seat_layout, null)
        }

        return view!!
    }

}