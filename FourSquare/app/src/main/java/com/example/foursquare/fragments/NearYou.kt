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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foursquare.adapter.NearYouRecyclerViewAdapter
import com.example.foursquare.R
import com.example.foursquare.databinding.FragmentNearYouBinding
import com.example.foursquare.viewmodels.LoginViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import androidx.lifecycle.lifecycleScope
import com.example.foursquare.api.RetrofitClient
import com.example.foursquare.dataclass.*
import com.example.foursquare.storage.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NearYou() : Fragment() {

    private lateinit var nearYouBinding: FragmentNearYouBinding
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private lateinit var latLangg: LatLangg

    private lateinit var viewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        nearYouBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_near_you, container, false)

        val latitude = arguments?.getDouble("latitude")
        val longitude = arguments?.getDouble("longitude")

        latitude?.let { lat ->
            longitude?.let { lon ->
                latLangg = LatLangg(lat, lon)
            }
        }

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback { googleMap1 ->
            googleMap = googleMap1

            googleMap.isMyLocationEnabled = true

            val locationLatLng =
                latLangg.let { it1 -> LatLng(it1.latitude, it1.longitude) }

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 18f))

            //initViewModel()
            getNearYouPlaces(latLangg)

        })

        return nearYouBinding.root
    }


    private fun getNearYouPlaces(latLangg: LatLangg) {
        RetrofitClient.instance.nearYou(latLangg)
            .enqueue(object : Callback<NearYouResponse> {
                override fun onResponse(
                    call: Call<NearYouResponse>,
                    response: Response<NearYouResponse>
                ) {
                    val it = response.body()
                    if (it == null) {
                        Toast.makeText(this@NearYou.context, "No Places", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("response", it.toString())

                        val getFavouritesRequest =
                            GetFavouritesRequest(latLangg.latitude, latLangg.longitude, "")
                        val sharedPreferencesManager =
                            SharedPreferencesManager.getInstance(nearYouBinding.root.context)
                        val token = sharedPreferencesManager.tokenManager.token
                        if (sharedPreferencesManager.isLoggedIn) {
                            if (token != null) {
                                initViewModel1(it)
                                getFavourites(token, getFavouritesRequest)
                            }
                        } else {
                            nearYouBinding.recyclerview.layoutManager =
                                LinearLayoutManager(nearYouBinding.root.context)
                            nearYouBinding.recyclerview.adapter =
                                NearYouRecyclerViewAdapter(
                                    activity,
                                    it,
                                    lifecycleScope,
                                    viewLifecycleOwner,
                                    this@NearYou,
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
                    //Toast.makeText(this@Details.context, "No Places", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("response", it.toString())

                    nearYouBinding.recyclerview.layoutManager =
                        LinearLayoutManager(nearYouBinding.root.context)
                    nearYouBinding.recyclerview.adapter =
                        NearYouRecyclerViewAdapter(
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

        //initViewModel()
        getNearYouPlaces(latLangg)
    }
}