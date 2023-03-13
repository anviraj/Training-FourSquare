package com.example.foursquare.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foursquare.R
import com.example.foursquare.dataclass.GetFavouritesResponse
import com.example.foursquare.dataclass.GetNearByPlacesResponse

class SearchNearByYouRecyclerViewAdapter(
    private val getNearByPlacesResponse: GetNearByPlacesResponse,
    private val searchSv: SearchView
) : RecyclerView.Adapter<SearchNearByYouRecyclerViewAdapter.ViewHolder>() {


    private lateinit var context: Context

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val placeImage: ImageView = itemView.findViewById(R.id.placeImage_iv)
        val placeName: TextView = itemView.findViewById(R.id.placeName_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_near_by_places_data_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getNearByPlacesResponse[position]

        var url = data.image

        if (!url.contains("https"))
            url = "${url.substring(0..3)}s${url.substring(4)}"

        Glide.with(context).load(url).into(holder.placeImage)
        holder.placeName.text = data.city

        holder.itemView.setOnClickListener {
            searchSv.setQuery(data.city, false)
        }
    }

    override fun getItemCount(): Int {
        return getNearByPlacesResponse.size
    }
}
