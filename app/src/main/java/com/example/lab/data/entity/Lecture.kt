package com.example.lab.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalTime


data class Lecture(
    /**
     * primary key로 활용되는 id
     */
    @Expose
    @SerializedName("id")
    val id: Long? = null,

    /**
     * 강의 고유 코드
     */
    @Expose
    @SerializedName("code")
    var code: String? = null,

    /**
     * 강의의 이름
     */
    @Expose
    @SerializedName("title")
    var title: String? = null,

    /**
     * 강의 담당 교수
     */
    @Expose
    @SerializedName("professor")
    var professor: String? = null,

    /**
     * 강의실 호수
     */
    @Expose
    @SerializedName("roomNumber")
    var place: String? = null,
    
    /**
     * 정규 수업의 경우 개강 날짜
     */
    @Expose
    @SerializedName("startDate")
    var startDate: String? = null,

    /**
     * 정규 수업의 경우 종강 날짜
     */
    @Expose
    @SerializedName("endDate")
    var endDate: String? = null,

    /**
     * 강의 시작 시간
     */
    @Expose
    @SerializedName("startTime")
    var startTime: String? = null,

    /**
     * 강의 종료 시간
     */
    @Expose
    @SerializedName("endTime")
    var endTime: String? = null,

    /**
     * 강의 시작 요일
     */
    @Expose
    @SerializedName("day")
    var day: String? = null,
)