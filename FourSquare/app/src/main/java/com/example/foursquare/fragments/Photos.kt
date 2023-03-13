package com.example.foursquare.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.Toast
import androidx.core.provider.FontsContractCompat.Columns
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.foursquare.R
import com.example.foursquare.adapter.PhotosGVAdapter
import com.example.foursquare.databinding.FragmentPhotosBinding
import com.example.foursquare.dataclass.GetReviewImageResponse
import com.example.foursquare.dataclass.Id
import com.example.foursquare.storage.SharedPreferencesManager
import com.example.foursquare.viewmodels.LoginViewModel

class Photos : Fragment() {

    private lateinit var photosBinding: FragmentPhotosBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        photosBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_photos, container, false)

        val id = arguments?.getString("id").toString()

        photosBinding.nameTv.text = arguments?.getString("place_name").toString()
        initViewModel()
        getReviewImage(id)

        photosBinding.addImageIv.setOnClickListener {
            val sharedPreferencesManager =
                SharedPreferencesManager.getInstance(photosBinding.root.context)
            if (sharedPreferencesManager.isLoggedIn) {
                val bundle = Bundle()
                bundle.putString("id", id)
                val addReview = AddReview()
                addReview.arguments = bundle
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, addReview)?.addToBackStack(null)?.commit()
            } else {
                Toast.makeText(
                    photosBinding.root.context,
                    "please login to give review",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        photosBinding.backIv.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        return photosBinding.root
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getReviewImageObserver()
            .observe(viewLifecycleOwner, Observer<GetReviewImageResponse?> {

                if (it == null) {
                } else {
                    Log.d("response", it.toString())

                    val reviewImageList = mutableListOf<String>()
                    for (i in it.reviewImage) {
                        reviewImageList.addAll(i.image)
                    }

                    val adapter = PhotosGVAdapter(
                        photosBinding.root.context,
                        it.reviewImage,
                        reviewImageList,
                        lifecycleScope,
                        activity,
                        photosBinding.nameTv.text.toString()
                    )
                    photosBinding.photosGv.adapter = adapter
                }
            })
    }

    private fun getReviewImage(id: String) {
        val idObject = Id(id)
        viewModel.getReviewImage(idObject)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            photosBinding.photosGv.numColumns = 5
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            photosBinding.photosGv.numColumns = 3
        }
    }
}