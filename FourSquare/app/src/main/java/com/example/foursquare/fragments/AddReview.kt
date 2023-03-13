package com.example.foursquare.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.foursquare.R
import com.example.foursquare.adapter.AddPhotosGVAdapter
import com.example.foursquare.api.RetrofitClient
import com.example.foursquare.databinding.FragmentAddReviewBinding
import com.example.foursquare.dataclass.AddFavouriteResponse
import com.example.foursquare.dataclass.ImageList
import com.example.foursquare.dataclass.ReviewImageRequest
import com.example.foursquare.storage.SharedPreferencesManager
import com.example.foursquare.viewmodels.LoginViewModel
import com.robosoft.crickapp.services.RealPathUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class AddReview : Fragment() {

    private lateinit var addReviewBinding: FragmentAddReviewBinding
    private lateinit var viewModel: LoginViewModel
    private var imagesList = mutableListOf<Uri>()
    private var imagePathes = mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addReviewBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_review, container, false)

        val id = arguments?.getString("id").toString()

        addReviewBinding.backIv.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        addReviewBinding.addImageIv.setOnClickListener {
            if (checkPermissions(activity)) {
                Log.d("permission", "granted")
                val intent = Intent()
                intent.apply {
                    type = "image/*"
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    action = Intent.ACTION_GET_CONTENT
                }

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
            }
        }


        addReviewBinding.submitTv.setOnClickListener {
            val reviewText = addReviewBinding.reviewEt.text.toString()
            if (reviewText.isNotEmpty()) {
                val sharedPreferencesManager =
                    SharedPreferencesManager.getInstance(addReviewBinding.root.context)
                val token = sharedPreferencesManager.tokenManager.token
                if (sharedPreferencesManager.isLoggedIn) {
                    if (token != null) {
                        val request = mapOf<String, String>("_id" to id, "review" to reviewText)
                        addReviewText(token, request)
                    }
                }
            }

            if (imagePathes.isNotEmpty()) {
                val sharedPreferencesManager =
                    SharedPreferencesManager.getInstance(addReviewBinding.root.context)
                val token = sharedPreferencesManager.tokenManager.token
                if (sharedPreferencesManager.isLoggedIn) {
                    if (token != null) {
                        val images = mutableListOf<ImageList>()
                        for (i in imagePathes) {
                            val file: File = File(i)
                            val requestFile =
                                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                            val body: MultipartBody.Part =
                                MultipartBody.Part.createFormData("image", file.name, requestFile)
                            images.add(ImageList(body))
                        }

                        val id = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), id)
                        val reviewImageRequest = ReviewImageRequest(images)

                        addReviewImages(token, id, reviewImageRequest.imagesList)
                    }
                }
            }
        }
        return addReviewBinding.root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            addReviewBinding.addPhotosGv.numColumns = 8
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            addReviewBinding.addPhotosGv.numColumns = 4
        }
    }

    private fun checkPermissions(fragmentActivity: FragmentActivity?): Boolean {
        return if (ContextCompat.checkSelfPermission(
                addReviewBinding.root.context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (activity != null) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PackageManager.PERMISSION_GRANTED
                )
            }

            false
        } else {
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data?.clipData != null) {
                val a = data.clipData?.itemCount

                a?.let {
                    Log.d("it", it.toString())
                    for (i in 0 until it) {
                        data.clipData?.getItemAt(i)?.uri?.let { it1 ->
                            imagesList.add(it1)
                            val path = RealPathUtil.getRealPath(addReviewBinding.root.context, it1)
                            if (path != null) {
                                imagePathes.add(path)
                            }
                        }
                        if (i == it - 1) {
                            val uri: Uri =
                                Uri.parse("android.resource://" + "com.example.foursquare.fragments" + "/" + R.drawable.aad_photo_icon)
                            imagesList.add(uri)
                        }
                    }
                }


                addReviewBinding.addPhotosGv.adapter = AddPhotosGVAdapter(
                    addReviewBinding.root.context,
                    imagesList,
                    addReviewBinding,
                    lifecycleScope,
                    activity
                )

                addReviewBinding.addImageIv.visibility = View.GONE
                addReviewBinding.addPhotosGv.visibility = View.VISIBLE
            }
        }
    }


    private fun addReviewText(token: String, request: Map<String, String>) {
        RetrofitClient.instance.addReviewText(token, request)
            .enqueue(object : Callback<AddFavouriteResponse> {
                override fun onResponse(
                    call: Call<AddFavouriteResponse>,
                    response: Response<AddFavouriteResponse>
                ) {
                    val it = response.body()
                    if (it == null) {
                    } else {
                        Log.d("response", it.toString())
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AddFavouriteResponse>, t: Throwable) {

                }

            })

    }


    private fun addReviewImages(
        token: String,
        id: RequestBody,
        imagesList: MutableList<ImageList>
    ) {
        RetrofitClient.instance.addReviewImages(token, id, imagesList)
            .enqueue(object : Callback<AddFavouriteResponse> {
                override fun onResponse(
                    call: Call<AddFavouriteResponse>,
                    response: Response<AddFavouriteResponse>
                ) {
                    val it = response.body()
                    if (it == null) {
                    } else {
                        Log.d("response", it.toString())
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AddFavouriteResponse>, t: Throwable) {

                }

            })

    }
}