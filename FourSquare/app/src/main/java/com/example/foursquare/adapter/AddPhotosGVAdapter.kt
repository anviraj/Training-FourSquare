package com.example.foursquare.adapter

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleCoroutineScope
import com.bumptech.glide.Glide
import com.example.foursquare.R
import com.example.foursquare.databinding.FragmentAddReviewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

internal class AddPhotosGVAdapter(
    private val context: Context,
    private val imagesList: MutableList<Uri>,
    private val addReviewBinding: FragmentAddReviewBinding,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val activity: FragmentActivity?
) :
    BaseAdapter() {

    private var layoutInflater: LayoutInflater? = null

    private lateinit var imageIv: ImageView

    override fun getCount(): Int {
        return imagesList.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.add_review_datalist_item, null)


            imageIv = convertView!!.findViewById(R.id.image_iv)
            convertView.setOnClickListener {
                if (position == imagesList.size - 1) {

                }
            }
            Glide.with(context).load(imagesList[position]).into(imageIv)

        }
        return convertView
    }






}

