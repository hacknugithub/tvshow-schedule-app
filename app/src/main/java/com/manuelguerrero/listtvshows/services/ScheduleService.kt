package com.manuelguerrero.listtvshows.services

import com.manuelguerrero.listtvshows.models.Cast
import com.manuelguerrero.listtvshows.models.MySchedule
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ScheduleService {

    @GET("schedule")
    fun getScheduleTvShowsList(
        @Query("country") country: String,
        @Query("date") date: String
    ): Call<List<MySchedule>>

    @GET("search/shows") fun getSearchTvShowsList(
            @Query("q") query: String
    ): Call<List<MySchedule>>

    @GET("shows/{id}") fun getSingleShow(@Path("id") id: String) :Call<MySchedule.Show>

    @GET("shows/{id}/cast") fun getShowCast(@Path("id") id: String) :Call<List<Cast>>
}