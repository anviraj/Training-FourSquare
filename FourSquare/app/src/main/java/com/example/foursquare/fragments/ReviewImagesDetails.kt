package com.example.foursquare.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.foursquare.R
import com.example.foursquare.databinding.FragmentPhotosBinding
import com.example.foursquare.databinding.FragmentReviewImagesDetailsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

class ReviewImagesDetails : Fragment() {

    private lateinit var reviewImagesDetailsBinding: FragmentReviewImagesDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reviewImagesDetailsBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_review_images_details,
                container,
                false
            )

        val reviewerName = arguments?.getString("reviewer_name").toString()
        val reviewDate = arguments?.getString("review_date").toString()
        var url = arguments?.getString("url").toString()
        if (!url.contains("https"))
            url = "${url.substring(0..3)}s${url.substring(4)}"
        val placeName = arguments?.getString("placeName").toString()
        val reviewerImage = arguments?.getString("reviewerImage").toString()

        reviewImagesDetailsBinding.apply {
            closeIv.setOnClickListener {
                activity?.supportFragmentManager?.popBackStack()
            }

            nameTv.text = placeName
            name1Tv.text = reviewerName

            val datee = reviewDate.substringBefore("T").split("-")
            val month = when (datee[1].toInt()) {
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
            name2Tv.text = "Added $month ${datee[2]},${datee[0]}"

            Glide.with(reviewImagesDetailsBinding.root.context).load(reviewerImage).into(profileIv)
            loadImageFromWebUrl(url, selectedImageIv)
        }

        return reviewImagesDetailsBinding.root
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