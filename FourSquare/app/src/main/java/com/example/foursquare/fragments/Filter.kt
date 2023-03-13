package com.example.foursquare.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foursquare.R
import com.example.foursquare.adapter.FilterSearchRecyclerViewAdapter
import com.example.foursquare.api.RetrofitClient
import com.example.foursquare.databinding.FragmentFilterBinding
import com.example.foursquare.dataclass.*
import com.example.foursquare.interfaces.Communicator
import com.example.foursquare.storage.SharedPreferencesManager
import com.example.foursquare.viewmodels.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Filter : Fragment() {

    private lateinit var filterBinding: FragmentFilterBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var latLangg: LatLangg
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        filterBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_filter, container, false)

        val latitude = arguments?.getDouble("latitude") as Double
        val longitude = arguments?.getDouble("longitude") as Double

        latitude?.let { lat ->
            longitude?.let { lon ->
                latLangg = LatLangg(lat, lon)
            }
        }

        val preferences = mutableMapOf<String, Any>()
        filterBinding.apply {

            backIv.setOnClickListener {
                if (arguments?.getString("from") == "Container") {
                    val communicator = activity as Communicator
                    communicator.gotoHome()
                } else {
                    activity?.supportFragmentManager?.popBackStack()
                }
            }

            var popular = false
            var distance = false
            var rating = false

            popularIv.setOnClickListener {
                if (!popular) {
                    it.background = resources.getDrawable(R.drawable.popular_selected)
                    distanceIv.background = resources.getDrawable(R.drawable.distance)
                    ratingIv.background = resources.getDrawable(R.drawable.rating)
                    popular = true
                    distance = false
                    rating = false
                } else {
                    it.background = resources.getDrawable(R.drawable.popular)
                    popular = false
                }

                Log.d("popular", "$popular, $distance, $rating")
            }

            distanceIv.setOnClickListener {
                if (!distance) {
                    it.background = resources.getDrawable(R.drawable.distance_selected)
                    popularIv.background = resources.getDrawable(R.drawable.popular)
                    ratingIv.background = resources.getDrawable(R.drawable.rating)
                    distance = true
                    popular = false
                    rating = false
                } else {
                    it.background = resources.getDrawable(R.drawable.distance)
                    distance = false
                }

                Log.d("distance", "$popular, $distance, $rating")
            }

            ratingIv.setOnClickListener {
                if (!rating) {
                    it.background = resources.getDrawable(R.drawable.rating_selected)
                    popularIv.background = resources.getDrawable(R.drawable.popular)
                    distanceIv.background = resources.getDrawable(R.drawable.distance)
                    rating = true
                    popular = false
                    distance = false
                } else {
                    it.background = resources.getDrawable(R.drawable.rating)
                    rating = false
                }

                Log.d("rating", "$popular, $distance, $rating")
            }

            var rupee1 = false
            var rupee2 = false
            var rupee3 = false
            var rupee4 = false
            rupee1Iv.setOnClickListener {
                if (!rupee1) {
                    it.background = resources.getDrawable(R.drawable.ruppe_btn1_selected)
                    rupee2Iv.background = resources.getDrawable(R.drawable.ruppe_btn2)
                    rupee3Iv.background = resources.getDrawable(R.drawable.ruppe_btn3)
                    rupee4Iv.background = resources.getDrawable(R.drawable.ruppe_btn4)
                    rupee1 = true
                    rupee2 = false
                    rupee3 = false
                    rupee4 = false
                } else {
                    it.background = resources.getDrawable(R.drawable.ruppe_btn1)
                    rupee1 = false
                }
            }

            rupee2Iv.setOnClickListener {
                if (!rupee2) {
                    it.background = resources.getDrawable(R.drawable.ruppe_btn2_selected)
                    rupee1Iv.background = resources.getDrawable(R.drawable.ruppe_btn1)
                    rupee3Iv.background = resources.getDrawable(R.drawable.ruppe_btn3)
                    rupee4Iv.background = resources.getDrawable(R.drawable.ruppe_btn4)
                    rupee1 = false
                    rupee2 = true
                    rupee3 = false
                    rupee4 = false
                } else {
                    it.background = resources.getDrawable(R.drawable.ruppe_btn2)
                    rupee2 = false
                }
            }

            rupee3Iv.setOnClickListener {
                if (!rupee3) {
                    it.background = resources.getDrawable(R.drawable.ruppe_btn3_selected)
                    rupee1Iv.background = resources.getDrawable(R.drawable.ruppe_btn1)
                    rupee2Iv.background = resources.getDrawable(R.drawable.ruppe_btn2)
                    rupee4Iv.background = resources.getDrawable(R.drawable.ruppe_btn4)
                    rupee1 = false
                    rupee2 = false
                    rupee3 = true
                    rupee4 = false
                } else {
                    it.background = resources.getDrawable(R.drawable.ruppe_btn3)
                    rupee3 = false
                }
            }

            rupee4Iv.setOnClickListener {
                if (!rupee4) {
                    it.background = resources.getDrawable(R.drawable.ruppe_btn4_selected)
                    rupee1Iv.background = resources.getDrawable(R.drawable.ruppe_btn1)
                    rupee2Iv.background = resources.getDrawable(R.drawable.ruppe_btn2)
                    rupee3Iv.background = resources.getDrawable(R.drawable.ruppe_btn3)
                    rupee1 = false
                    rupee2 = false
                    rupee3 = false
                    rupee4 = true
                } else {
                    it.background = resources.getDrawable(R.drawable.ruppe_btn4)
                    rupee4 = false
                }

            }

            var acceptCreditCards = false
            var delivery = false
            var dogFriendly = false
            var familyFriendlyPlaces = false
            var inWalkingDistance = false
            var outdoorSeating = false
            var parking = false
            var wifi = false
            acceptCreditCardsTv.setOnClickListener {
                if (!acceptCreditCards) {
                    acceptCreditCardsTv.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.filter_selected,
                        0
                    )
                    acceptCreditCardsTv.setTextColor(resources.getColor(R.color.black))
                    acceptCreditCards = true
                    preferences["acceptedCredit"] = true
                } else {
                    acceptCreditCardsTv.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_baseline_add_24,
                        0
                    )
                    acceptCreditCardsTv.setTextColor(resources.getColor(R.color._8D8D8D))
                    acceptCreditCards = false
                    preferences.remove("acceptedCredit")
                }
            }

            deliveryTv.setOnClickListener {
                if (!delivery) {
                    deliveryTv.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.filter_selected,
                        0
                    )
                    deliveryTv.setTextColor(resources.getColor(R.color.black))
                    delivery = true
                    preferences["delivery"] = true
                } else {
                    deliveryTv.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_baseline_add_24,
                        0
                    )
                    deliveryTv.setTextColor(resources.getColor(R.color._8D8D8D))
                    delivery = false
                    preferences.remove("delivery")
                }
            }

            dogFriendlyTv.setOnClickListener {
                if (!dogFriendly) {
                    dogFriendlyTv.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.filter_selected,
                        0
                    )
                    dogFriendlyTv.setTextColor(resources.getColor(R.color.black))
                    dogFriendly = true
                    preferences["dogFriendly"] = true
                } else {
                    dogFriendlyTv.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_baseline_add_24,
                        0
                    )
                    dogFriendlyTv.setTextColor(resources.getColor(R.color._8D8D8D))
                    dogFriendly = false
                    preferences.remove("dogFriendly")
                }
            }

            familyFriendlyPlacesTv.setOnClickListener {
                if (!familyFriendlyPlaces) {
                    familyFriendlyPlacesTv.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.filter_selected,
                        0
                    )
                    familyFriendlyPlacesTv.setTextColor(resources.getColor(R.color.black))
                    familyFriendlyPlaces = true
                    preferences["familyFriendly"] = true
                } else {
                    familyFriendlyPlacesTv.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_baseline_add_24,
                        0
                    )
                    familyFriendlyPlacesTv.setTextColor(resources.getColor(R.color._8D8D8D))
                    familyFriendlyPlaces = false
                    preferences.remove("familyFriendly")
                }
            }

            inWalkingDistanceTv.setOnClickListener {
                if (!inWalkingDistance) {
                    inWalkingDistanceTv.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.filter_selected,
                        0
                    )
                    inWalkingDistanceTv.setTextColor(resources.getColor(R.color.black))
                    inWalkingDistance = true
                    preferences["inWalkingDistance"] = true
                } else {
                    inWalkingDistanceTv.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_baseline_add_24,
                        0
                    )
                    inWalkingDistanceTv.setTextColor(resources.getColor(R.color._8D8D8D))
                    inWalkingDistance = false
                    preferences.remove("inWalkingDistance")
                }
            }

            outdoorSeatingTv.setOnClickListener {
                if (!outdoorSeating) {
                    outdoorSeatingTv.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.filter_selected,
                        0
                    )
                    outdoorSeatingTv.setTextColor(resources.getColor(R.color.black))
                    outdoorSeating = true
                    preferences["outdoorDining"] = true
                } else {
                    outdoorSeatingTv.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_baseline_add_24,
                        0
                    )
                    outdoorSeatingTv.setTextColor(resources.getColor(R.color._8D8D8D))
                    outdoorSeating = false
                    preferences.remove("outdoorDining")
                }
            }

            parkingTv.setOnClickListener {
                if (!parking) {
                    parkingTv.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.filter_selected,
                        0
                    )
                    parkingTv.setTextColor(resources.getColor(R.color.black))
                    parking = true
                    preferences["parking"] = true
                } else {
                    parkingTv.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_baseline_add_24,
                        0
                    )
                    parkingTv.setTextColor(resources.getColor(R.color._8D8D8D))
                    parking = false
                    preferences.remove("parking")
                }
            }

            wifiTv.setOnClickListener {
                if (!wifi) {
                    wifiTv.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.filter_selected,
                        0
                    )
                    wifiTv.setTextColor(resources.getColor(R.color.black))
                    wifi = true
                    preferences["wifi"] = true
                } else {
                    wifiTv.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.ic_baseline_add_24,
                        0
                    )
                    wifiTv.setTextColor(resources.getColor(R.color._8D8D8D))
                    wifi = false
                    preferences.remove("wifi")
                }
            }

            doneTv.setOnClickListener {
                val radius = setRadiusEt.text.toString()

                if (radius.contains("km"))
                    radius.substringBefore("km")

                if (radius.isNotEmpty() && radius != "0") {
                    preferences["radius"] = radius.toFloat()
                }

                preferences["latitude"] = latitude
                preferences["longitude"] = longitude

                preferences["sortBy"] =
                    if (popular) "popularityCount"
                    else if (rating) "rating"
                    else "distance"

                preferences["price"] =
                    if (rupee1) 1
                    else if (rupee2) 2
                    else if (rupee3) 3
                    else if (rupee4) 4
                    else ""

                preferences["text"] = searchSv.query.toString()

                Log.d("filter", preferences.toString())

                //initViewModel()
                getFilterSearch(preferences)
            }
        }

        return filterBinding.root
    }


    private fun getFilterSearch(preferences: MutableMap<String, Any>) {
        RetrofitClient.instance.getFilterSearch(preferences)
            .enqueue(object : Callback<GetFilterSearchResponse> {
                override fun onResponse(
                    call: Call<GetFilterSearchResponse>,
                    response: Response<GetFilterSearchResponse>
                ) {
                    val it = response.body()
                    if (it == null) {
                    } else {
                        Log.d("response", it.toString())
                        val getFavouritesRequest =
                            GetFavouritesRequest(latLangg.latitude, latLangg.longitude, "")
                        val sharedPreferencesManager =
                            SharedPreferencesManager.getInstance(filterBinding.root.context)
                        val token = sharedPreferencesManager.tokenManager.token
                        if (sharedPreferencesManager.isLoggedIn) {
                            if (token != null) {
                                initViewModel1(it)
                                getFavourites(token, getFavouritesRequest)
                            }
                        } else {
                            filterBinding.scrollView.visibility = View.GONE
                            filterBinding.recyclerview.layoutManager = LinearLayoutManager(context)
                            filterBinding.recyclerview.adapter =
                                FilterSearchRecyclerViewAdapter(activity, it, lifecycleScope, null)

                        }
                    }
                }

                override fun onFailure(call: Call<GetFilterSearchResponse>, t: Throwable) {

                }

            })

    }

    private fun initViewModel1(getFilterSearchResponse: GetFilterSearchResponse) {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getFavouritesObserver()
            .observe(viewLifecycleOwner, Observer<GetFavouritesResponse?> {

                if (it == null) {
                } else {
                    Log.d("response", it.toString())

                    filterBinding.scrollView.visibility = View.GONE
                    filterBinding.recyclerview.layoutManager = LinearLayoutManager(context)
                    filterBinding.recyclerview.adapter =
                        FilterSearchRecyclerViewAdapter(
                            activity,
                            getFilterSearchResponse,
                            lifecycleScope,
                            it
                        )
                }
            })
    }

    private fun getFavourites(token: String, getFavouritesRequest: GetFavouritesRequest) {
        viewModel.getFavourites(token, getFavouritesRequest)
    }
}