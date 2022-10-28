package com.manuelguerrero.listtvshows.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.VISIBLE
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.manuelguerrero.listtvshows.R
import com.manuelguerrero.listtvshows.helpers.CrewAdapter
import com.manuelguerrero.listtvshows.models.Cast
import com.manuelguerrero.listtvshows.models.MySchedule
import com.manuelguerrero.listtvshows.services.ScheduleService
import com.manuelguerrero.listtvshows.services.ServiceBuilder
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShowTvDetailActivity : AppCompatActivity() {
    var ivDetailImage: ImageView? = null
    var tvDetailTitle: TextView? = null
    var tvNetwork: TextView? = null
    var tvDetailRating: TextView? = null
    var btnViewSite: MaterialButton? = null
    var tvShowDetail: TextView? = null
    var tvShowGenre: TextView? = null
    var tvShowSchedule: TextView? = null
    var webView: WebView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_show_detail)

        val bundle: Bundle? = intent.extras

        ivDetailImage = findViewById(R.id.ivDetailShowImage)
        tvDetailTitle = findViewById(R.id.tvDetailShowTitle)
        tvNetwork = findViewById(R.id.tvDetailShowNetwork)
        tvDetailRating = findViewById(R.id.tvDetailRating)
        btnViewSite = findViewById(R.id.tvSiteBtn)
        tvShowDetail = findViewById(R.id.tvShowDetail)
        tvShowGenre = findViewById(R.id.tvShowGenre)
        tvShowSchedule = findViewById(R.id.tvShowSchedule)
        webView = findViewById(R.id.wvSite)

        bundle?.apply {
            val showId: String? = getString("showId")
            loadShow(showId)

            val castRecyclerView = findViewById<RecyclerView>(R.id.rv_cast)
            if (showId != null) {
                loadShowCast(castRecyclerView, showId)
            }
        }
    }

    private fun loadShow(showId: String?){

        val destinationService = ServiceBuilder.buildService(ScheduleService::class.java)
        val requestCall = destinationService.getSingleShow(showId.toString())

        requestCall.enqueue(object : Callback<MySchedule.Show> {
            @SuppressLint("SetJavaScriptEnabled")
            override fun onResponse(
                call: Call<MySchedule.Show>,
                response: Response<MySchedule.Show>
            ) {
                Log.d("Response", "Show onResponse: ${response.body()}")
                if (response.isSuccessful) {
                    val showItem = response.body()!!
                    val res = baseContext.resources
                    Picasso.get().load(showItem.image?.medium).fit().into(ivDetailImage)
                    tvDetailTitle?.text = showItem.name
                    tvNetwork?.text = showItem.network?.name
                    tvDetailRating?.text = String.format(
                        res.getString(R.string.tv_rating_detail),
                        showItem.rating.average
                    )
                    val summarySanitized = Html.fromHtml(showItem.summary).toString()
                    tvShowDetail?.text = String.format(
                        res.getString(R.string.title_activity_tv_show_detail),
                        summarySanitized
                    )
                    tvShowGenre?.text = String.format(
                        res.getString(R.string.tv_genre_detail),
                        showItem.genres.joinToString()
                    )
                    tvShowSchedule?.text = String.format(
                        res.getString(R.string.tv_schedule_detail), showItem.schedule.time,
                        showItem.schedule.days.joinToString()
                    )


                    val btnMaterial: MaterialButton = findViewById(R.id.tvSiteBtn)
                    btnMaterial.setOnClickListener(fun(it: View) {
                        val webSettings: WebSettings = webView!!.settings
                        webSettings.javaScriptEnabled = true
                        val baseUrl: String = showItem.officialSite.toString()
                        webView!!.webViewClient = WebViewClient()
                        webView!!.visibility = VISIBLE
                        webView!!.bringToFront()
                        showItem.officialSite?.let { this@ShowTvDetailActivity.webView!!.loadUrl(it) }
                    })


                } else {
                    Toast.makeText(
                        this@ShowTvDetailActivity,
                        "Something broke ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<MySchedule.Show>, t: Throwable) {
                Toast.makeText(
                    this@ShowTvDetailActivity,
                    "Something went wrong $t",
                    Toast.LENGTH_LONG
                ).show()
            }


        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView!!.canGoBack()) {
            this.webView!!.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun loadShowCast(recyclerView: RecyclerView, showId: String){

        val destinationService = ServiceBuilder.buildService(ScheduleService::class.java)
        val requestCall = destinationService.getShowCast(showId)

        requestCall.enqueue(object : Callback<List<Cast>> {
            override fun onResponse(call: Call<List<Cast>>, response: Response<List<Cast>>) {
                if (response.isSuccessful) {
                    val castList = response.body()!!
                    recyclerView.apply {
                        setHasFixedSize(true)
                        layoutManager = GridLayoutManager(this@ShowTvDetailActivity, 3)
                        adapter = CrewAdapter(castList)
                    }
                } else {
                    Toast.makeText(
                        this@ShowTvDetailActivity,
                        "Something went wrong ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Cast>>, t: Throwable) {
                Toast.makeText(
                    this@ShowTvDetailActivity,
                    "Something went wrong ${t}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

    }
}