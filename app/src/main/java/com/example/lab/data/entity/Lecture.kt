package com.example.lab.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalTime


class Lecture {
    /**
     * primary key로 활용되는 id
     */
    @Expose
    @SerializedName("id")
    private val id: Long? = null

    @Expose
    @SerializedName("lab")
    private val lab: Lab? = null

    /**
     * 강의의 이름
     */
    @Expose
    @SerializedName("title")
    private val title: String? = null

    /**
     * 강의 담당 교수
     */
    @Expose
    @SerializedName("professor")
    private val professor: String? = null

    /**
     * 강의 고유 코드
     */
    @Expose
    @SerializedName("code")
    private val code: String? = null

    /**
     * 정규 수업의 경우 개강 날짜
     */
    @Expose
    @SerializedName("startDate")
    private val startDate: LocalDate? = null

    /**
     * 정규 수업의 경우 종강 날짜
     */
    @Expose
    @SerializedName("endDate")
    private val endDate: LocalDate? = null

    /**
     * 강의 시작 시간
     */
    @Expose
    @SerializedName("startTime")
    private val startTime: LocalTime? = null

    /**
     * 강의 종료 시간
     */
    @Expose
    @SerializedName("endTime")
    private val endTime: LocalTime? = null

    /**
     * 강의 시작 요일
     */
    @Expose
    @SerializedName("day")
    private val day: String? = null
}