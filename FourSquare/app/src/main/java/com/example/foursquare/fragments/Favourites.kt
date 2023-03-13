package com.example.foursquare.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foursquare.R
import com.example.foursquare.adapter.FavouritesRecyclerViewAdapter
import com.example.foursquare.databinding.FragmentFavouritesBinding
import com.example.foursquare.dataclass.*
import com.example.foursquare.interfaces.Communicator
import com.example.foursquare.storage.SharedPreferencesManager
import com.example.foursquare.viewmodels.LoginViewModel

class Favourites : Fragment() {

    private lateinit var favouritesBinding: FragmentFavouritesBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var latLangg: LatLangg
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        favouritesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_favourites, container, false)

        val latitude = arguments?.getDouble("latitude")
        val longitude = arguments?.getDouble("longitude")

        latitude?.let { lat ->
            longitude?.let { lon ->
                latLangg = LatLangg(lat, lon)
            }
        }

        val getFavouritesRequest = GetFavouritesRequest(latLangg.latitude, latLangg.longitude, "")
        val token =
            SharedPreferencesManager.getInstance(favouritesBinding.root.context).tokenManager.token
        initViewModel()
        if (token != null) {
            getFavourites(token, getFavouritesRequest)
        }

        favouritesBinding.backIv.setOnClickListener {
            val communicator = activity as Communicator
            communicator.gotoHome()
        }

        favouritesBinding.searchSv.setOnQueryTextListener()

        return favouritesBinding.root
    }

    private fun SearchView.setOnQueryTextListener() {
        setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                val getFavouritesRequest =
                    GetFavouritesRequest(latLangg.latitude, latLangg.longitude, newText)
                val token =
                    SharedPreferencesManager.getInstance(favouritesBinding.root.context).tokenManager.token
                initViewModel()
                if (token != null) {
                    getFavourites(token, getFavouritesRequest)
                }

                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getFavouritesObserver()
            .observe(viewLifecycleOwner, Observer<GetFavouritesResponse?> {

                if (it == null) {
                } else {
                    Log.d("response", it.toString())

                    favouritesBinding.recyclerview.layoutManager =
                        LinearLayoutManager(favouritesBinding.root.context)
                    favouritesBinding.recyclerview.adapter =
                        FavouritesRecyclerViewAdapter(
                            activity,
                            it,
                            lifecycleScope,
                            viewLifecycleOwner,
                            this,
                            favouritesBinding.recyclerview
                        )
                }
            })
    }

    private fun getFavourites(token: String, getFavouritesRequest: GetFavouritesRequest) {
        viewModel.getFavourites(token, getFavouritesRequest)
    }

    override fun onResume() {
        super.onResume()
        val getFavouritesRequest = GetFavouritesRequest(latLangg.latitude, latLangg.longitude, "")
        val token =
            SharedPreferencesManager.getInstance(favouritesBinding.root.context).tokenManager.token
        initViewModel()
        if (token != null) {
            getFavourites(token, getFavouritesRequest)
        }
    }
}