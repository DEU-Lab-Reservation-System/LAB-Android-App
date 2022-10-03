package com.example.lab.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Lab {
    /**
     * primary key로 활용되는 id
     */

    @Expose
    @SerializedName("id")
    private val id: Long? = null

    /**
     * 실습실 호수 (방번호)
     */
    @Expose
    @SerializedName("roomNumber")
    private val roomNumber: String? = null

    /**
     * 살숩실의 수용 가능 인원
     */
    @Expose
    @SerializedName("capacity")
    private val capacity: Int? = null

    /**
     * 실습실에 열리는 강의 목록
     */
    @Expose
    @SerializedName("lectures")
    private val lectures: List<Lecture>? = null
}