package com.example.foursquare.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foursquare.R
import com.example.foursquare.activities.Container
import com.example.foursquare.dataclass.ReviewText
import com.example.foursquare.storage.SharedPreferencesManager
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.math.roundToInt

class ReviewRecyclerViewAdapter(
    private val activity: FragmentActivity?,
    private val reviewText: List<ReviewText>
) : RecyclerView.Adapter<ReviewRecyclerViewAdapter.ViewHolder>() {
    private lateinit var context: Context

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val profileImage: CircleImageView = itemView.findViewById(R.id.profile_iv)
        val reviewerName: TextView = itemView.findViewById(R.id.name_tv)
        val review: TextView = itemView.findViewById(R.id.review_tv)
        val date: TextView = itemView.findViewById(R.id.date_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_datalist_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = reviewText[position]


        var url = data.reviewerImage
        holder.apply {

            if (!url.contains("https"))
                url = "${url.substring(0..3)}s${url.substring(4)}"
            Glide.with(context).load(url).into(profileImage)

            reviewerName.text = data.reviewBy
            review.text = data.review

            val datee = data.reviewDate.substringBefore("T").split("-")
            val month = when(datee[1].toInt()){
                1 -> "Jan"
                2 -> "Feb"
                3 -> "Mar"
                4 -> "Apr"
                5 -> "May"
                6 -> "Jun"
                7 -> "Jul"
                8 -> "Aug"
                9 -> "Sep"
                10 -> "Oct"
                11 -> "Nov"
                else -> "Dec"
            }
            date.text = "$month ${datee[2]},${datee[0]}"
        }


    }

    override fun getItemCount(): Int {
        return reviewText.size
    }
}
