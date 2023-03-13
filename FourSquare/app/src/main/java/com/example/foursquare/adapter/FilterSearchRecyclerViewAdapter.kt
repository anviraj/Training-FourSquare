package com.example.foursquare.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.foursquare.R
import com.example.foursquare.activities.Container
import com.example.foursquare.dataclass.GetFavouritesResponse
import com.example.foursquare.dataclass.GetFilterSearchResponse
import com.example.foursquare.storage.SharedPreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import kotlin.math.roundToInt

class FilterSearchRecyclerViewAdapter(
    private val activity: FragmentActivity?,
    private val getFilterSearchResponse: GetFilterSearchResponse,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val getFavouritesResponse: GetFavouritesResponse?,
) :
    RecyclerView.Adapter<FilterSearchRecyclerViewAdapter.ViewHolder>() {

    private lateinit var context: Context

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val placeImage: ImageView = itemView.findViewById(R.id.placeImage_iv)
        val placeName: TextView = itemView.findViewById(R.id.placeName_tv)
        val rating: TextView = itemView.findViewById(R.id.rating_tv)
        val ratinglyt: LinearLayout = itemView.findViewById(R.id.rating_lyt)

        val description: TextView = itemView.findViewById(R.id.description_tv)
        val description1: TextView = itemView.findViewById(R.id.description1_tv)
        val favourite: ConstraintLayout = itemView.findViewById(R.id.favourite)
        val favouriteNotSelected: ImageView = itemView.findViewById(R.id.favourite_iv)
        val favouriteSelected: ImageView = itemView.findViewById(R.id.favouriteSelected_iv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.near_you_datalist_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = getFilterSearchResponse[position]
        var url = data.placeImage

        if (!url.contains("https"))
            url = "${url.substring(0..3)}s${url.substring(4)}"

        loadImageFromWebUrl(url, holder.placeImage)
        val distance = ((data.distance.calculated / 1609) * 100).roundToInt() / 100.0
        holder.apply {
            placeName.text = data.placeName
            rating.text = ((data.rating * 10).roundToInt() / 10.0).toString()
            val s = when (data.priceRange) {
                1 -> "₹"
                2 -> "₹₹"
                3 -> "₹₹₹"
                else -> "₹₹₹₹"
            }

            description.text = "${data.category} $s \t${distance}km"
            description1.text = data.address
        }

        holder.favouriteNotSelected.visibility = View.VISIBLE
        holder.favouriteSelected.visibility = View.GONE

        var favourite = false
        if (getFavouritesResponse != null) {
            for (i in getFavouritesResponse){
                if (data._id == i.placeId){
                    holder.favouriteNotSelected.visibility = View.GONE
                    holder.favouriteSelected.visibility = View.VISIBLE
                    favourite = true
                }

            }
        }

        holder.favourite.setOnClickListener {
            val isLoggedIn =
                SharedPreferencesManager.getInstance(context).isLoggedIn
            if (isLoggedIn) {
                if (!favourite) {
                    holder.favouriteNotSelected.visibility = View.GONE
                    holder.favouriteSelected.visibility = View.VISIBLE
                    favourite = true
                } else {
                    holder.favouriteNotSelected.visibility = View.VISIBLE
                    holder.favouriteSelected.visibility = View.GONE
                    favourite = false
                }
            } else {
                Toast.makeText(context, "Please login to save favourites", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        val hotel_rating = ((data.rating * 10).roundToInt() / 10.0).toString()
        if(hotel_rating >= 4.toString()) {
            holder.ratinglyt.setBackgroundResource(R.drawable.custom_rating_green)
        } else if (hotel_rating >= 2.toString()) {
            holder.ratinglyt.setBackgroundResource(R.drawable.custom_rating_yellow)
        } else {
            holder.ratinglyt.setBackgroundResource(R.drawable.custom_rating_orange)

        }

        holder.itemView.setOnClickListener {
            val intent = Intent(activity, Container::class.java)
            intent.putExtra("to", "Details")
            intent.putExtra("id", data._id)
            intent.putExtra("distance", distance)
            intent.putExtra("favourite", favourite)
            activity?.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return getFilterSearchResponse.size
    }

    private fun loadImageFromWebUrl(url: String, placeImage: ImageView) {
        lifecycleScope.launch(Dispatchers.IO) {
            val image = try {
                val iStream = java.net.URL(url).content as InputStream
                Drawable.createFromStream(iStream, "placeImage")
            } catch (e: Exception) {
                null
            }

            image?.let {
                withContext(Dispatchers.Main) {
                    placeImage.background = it
                }
            }
        }
    }

}