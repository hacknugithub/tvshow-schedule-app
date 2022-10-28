package com.manuelguerrero.listtvshows.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
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
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val currentDate: String = LocalDateTime.now().format(formatter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        topAppBar.title = baseContext.getString(R.string.page_title_date, currentDate)

        val tvShowRecycler: RecyclerView = findViewById(R.id.tv_show_recycler)

        loadSchedule(tvShowRecycler)

        val searchView = findViewById<SearchView>(R.id.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                Log.d("QueryTextSubmit", "Submitting: ${p0.toString()}")
                loadSchedule(tvShowRecycler, p0.toString())
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                Log.d("QueryTextChange", "Changing: ${p0.toString()}")
                return true
            }

        })

        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                Log.d("OnCloseListener", "Closing?")
                loadSchedule(tvShowRecycler)
                return false
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_bar_app, menu)

        return super.onCreateOptionsMenu(menu)
    }

    private fun showItemClicked(showItem: MySchedule.Show) {
        Toast.makeText(this, "Clicked: ${showItem.id}", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ShowTvDetailActivity::class.java)
        intent.putExtra("showId", showItem.id.toString())
        startActivity(intent)
    }

    private fun loadSchedule(recyclerTvView: RecyclerView, searchQuery: String = ""){

        val destinationService = ServiceBuilder.buildService(ScheduleService::class.java)
        val requestCall = if (searchQuery.isNotEmpty()) destinationService.getSearchTvShowsList(searchQuery) else destinationService.getScheduleTvShowsList("US", currentDate)

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
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = ScheduleAdapter(scheduleList) { showItem: MySchedule.Show -> showItemClicked(showItem) }
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