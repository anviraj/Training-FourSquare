package com.example.foursquare.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foursquare.R
import com.example.foursquare.adapter.NearYouRecyclerViewAdapter
import com.example.foursquare.api.RetrofitClient
import com.example.foursquare.databinding.FragmentCoffeeBinding
import com.example.foursquare.dataclass.GetFavouritesRequest
import com.example.foursquare.dataclass.GetFavouritesResponse
import com.example.foursquare.dataclass.LatLangg
import com.example.foursquare.dataclass.NearYouResponse
import com.example.foursquare.storage.SharedPreferencesManager
import com.example.foursquare.viewmodels.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Coffee() : Fragment() {

    private lateinit var coffeeBinding: FragmentCoffeeBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var latLangg: LatLangg

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        coffeeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_coffee, container, false)

        val latitude = arguments?.getDouble("latitude")
        val longitude = arguments?.getDouble("longitude")

        latitude?.let { lat ->
            longitude?.let { lon ->
                latLangg = LatLangg(lat, lon)
            }
        }

        getCoffeePlaces(latLangg)

        return coffeeBinding.root
    }


    private fun getCoffeePlaces(latLangg: LatLangg) {
        RetrofitClient.instance.coffee(latLangg)
            .enqueue(object : Callback<NearYouResponse> {
                override fun onResponse(
                    call: Call<NearYouResponse>,
                    response: Response<NearYouResponse>
                ) {
                    val it = response.body()
                    if (it == null) {
                        Toast.makeText(this@Coffee.context, "No Places", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("response", it.toString())

                        val getFavouritesRequest =
                            GetFavouritesRequest(latLangg.latitude, latLangg.longitude, "")
                        val sharedPreferencesManager =
                            SharedPreferencesManager.getInstance(coffeeBinding.root.context)
                        val token = sharedPreferencesManager.tokenManager.token
                        if (sharedPreferencesManager.isLoggedIn) {
                            if (token != null) {
                                initViewModel1(it)
                                getFavourites(token, getFavouritesRequest)
                            }
                        } else {
                            coffeeBinding.recyclerview.layoutManager =
                                LinearLayoutManager(coffeeBinding.root.context)
                            coffeeBinding.recyclerview.adapter =
                                NearYouRecyclerViewAdapter(
                                    activity,
                                    it,
                                    lifecycleScope,
                                    viewLifecycleOwner,
                                    this@Coffee,
                                    null
                                )
                        }
                    }
                }

                override fun onFailure(call: Call<NearYouResponse>, t: Throwable) {

                }

            })

    }

    private fun initViewModel1(nearYouResponse: NearYouResponse) {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getFavouritesObserver()
            .observe(viewLifecycleOwner, Observer<GetFavouritesResponse?> {

                if (it == null) {
                } else {
                    Log.d("response", it.toString())

                    coffeeBinding.recyclerview.layoutManager =
                        LinearLayoutManager(coffeeBinding.root.context)
                    coffeeBinding.recyclerview.adapter = NearYouRecyclerViewAdapter(
                        activity,
                        nearYouResponse,
                        lifecycleScope,
                        viewLifecycleOwner,
                        this,
                        it
                    )
                }
            })
    }

    private fun getFavourites(token: String, getFavouritesRequest: GetFavouritesRequest) {
        viewModel.getFavourites(token, getFavouritesRequest)
    }

    override fun onResume() {
        super.onResume()

        getCoffeePlaces(latLangg)
    }
}