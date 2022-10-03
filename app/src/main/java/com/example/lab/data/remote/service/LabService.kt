package com.example.lab.data.remote.service

import retrofit2.http.GET

interface LabService {

    @GET("api/labs/timetable")
    fun getTimetable()
}