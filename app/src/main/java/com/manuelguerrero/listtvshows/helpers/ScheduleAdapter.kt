package com.manuelguerrero.listtvshows.helpers

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.manuelguerrero.listtvshows.R
import com.manuelguerrero.listtvshows.models.MySchedule
import com.squareup.picasso.Picasso

class ScheduleAdapter(private val scheduleList: List<MySchedule>) :RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("Response", "List Count :${scheduleList.size}")

        return holder.bind(scheduleList[position])
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.findViewById<ImageView>(R.id.ivShowImage)
        var tvTitle = itemView.findViewById<TextView>(R.id.tvShowTitle)
        var tvNetwork = itemView.findViewById<TextView>(R.id.tvShowNetwork)
        var tvAirDate = itemView.findViewById<TextView>(R.id.tvShowAirDate)


        fun bind(scheduleItem: MySchedule) {
            tvTitle.text = scheduleItem.show.name
            tvNetwork.text = scheduleItem.show.network?.name

            val airDateString = itemView.context.getString(R.string.air_date_separator)
            Log.d("Air date string", airDateString)
            tvAirDate.text = String.format(airDateString, scheduleItem.airdate, scheduleItem.airtime)

            Picasso.get().load(scheduleItem.show.image?.medium).into(imageView)
        }
    }
}
