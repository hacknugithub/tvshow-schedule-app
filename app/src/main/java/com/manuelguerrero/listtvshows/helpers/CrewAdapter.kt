package com.manuelguerrero.listtvshows.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.manuelguerrero.listtvshows.R
import com.manuelguerrero.listtvshows.models.Cast
import com.squareup.picasso.Picasso

class CrewAdapter(private val castList: List<Cast>): RecyclerView.Adapter<CrewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cast_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(castList[position])
    }

    override fun getItemCount(): Int {
        return castList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val ivCrew: ImageView = itemView.findViewById(R.id.ivCrew)
        val tvCrewName: TextView = itemView.findViewById(R.id.tvCrewName)

        fun bind(cast: Cast) {
            tvCrewName.text = cast.person.name
            Picasso.get().load(cast.person.image.medium).into(ivCrew)
        }

    }
}