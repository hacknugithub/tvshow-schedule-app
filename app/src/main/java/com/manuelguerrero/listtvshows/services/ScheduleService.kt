package com.manuelguerrero.listtvshows.services

import com.manuelguerrero.listtvshows.models.MySchedule
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*


interface ScheduleService {

    @GET("schedule")
    fun getScheduleTvShowsList(
        @Query("country") country: String,
        @Query("date") date: String
    ): Call<List<MySchedule>>
}