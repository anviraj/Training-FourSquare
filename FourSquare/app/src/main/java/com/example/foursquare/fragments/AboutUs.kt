package com.example.foursquare.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.foursquare.R
import com.example.foursquare.api.RetrofitClient
import com.example.foursquare.databinding.FragmentAboutUsBinding
import com.example.foursquare.dataclass.GetAboutUsResponse
import com.example.foursquare.interfaces.Communicator
import com.example.foursquare.viewmodels.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AboutUs : Fragment() {


    private lateinit var aboutUsBinding: FragmentAboutUsBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        aboutUsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_about_us, container, false)

        aboutUsBinding.backIv.setOnClickListener {
            val communicator = activity as Communicator
            communicator.gotoHome()
        }

        getAboutUs()
        return aboutUsBinding.root
    }


    private fun getAboutUs() {
        RetrofitClient.instance.getAboutUs()
            .enqueue(object : Callback<GetAboutUsResponse> {
                override fun onResponse(
                    call: Call<GetAboutUsResponse>,
                    response: Response<GetAboutUsResponse>
                ) {
                    val it = response.body()
                    if (it == null) {
                    } else {
                        Log.d("response", it.toString())
                        aboutUsBinding.aboutUsTv.text = it[0].aboutUs
                    }
                }

                override fun onFailure(call: Call<GetAboutUsResponse>, t: Throwable) {

                }

            })

    }
}