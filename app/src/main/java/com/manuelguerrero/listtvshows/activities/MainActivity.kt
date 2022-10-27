package com.manuelguerrero.listtvshows.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.manuelguerrero.listtvshows.R
import com.manuelguerrero.listtvshows.helpers.ScheduleAdapter
import com.manuelguerrero.listtvshows.models.MySchedule
import com.manuelguerrero.listtvshows.services.ScheduleService
import com.manuelguerrero.listtvshows.services.ServiceBuilder
import retrofit2.Call
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv_show_recycler = findViewById<RecyclerView>(R.id.tv_show_recycler)
        loadSchedule(tv_show_recycler)
    }

    private fun loadSchedule(recyclerTvView: RecyclerView){
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val current = LocalDateTime.now().format(formatter)

        val destinationService = ServiceBuilder.buildService(ScheduleService::class.java)
        val requestCall = destinationService.getScheduleTvShowsList("US", current)

        requestCall.enqueue(object : Callback<List<MySchedule>>{
            override fun onResponse(
                call: Call<List<MySchedule>>,
                response: Response<List<MySchedule>>
            ) {
                Log.d("Response", "onResponse: ${response.body()}")
                if (response.isSuccessful){
                    val scheduleList = response.body()!!
                    recyclerTvView.apply {
                        setHasFixedSize(true)
                        layoutManager = GridLayoutManager(this@MainActivity, 2)
                        adapter = ScheduleAdapter(response.body()!!)
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Something broke ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<MySchedule>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Something went wrong $t", Toast.LENGTH_LONG).show()
            }

        })
    }
}