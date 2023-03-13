package com.example.foursquare.adapter

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.foursquare.dataclass.LatLangg
import com.example.foursquare.fragments.*

class ViewPagerAdapter(fragmentActivity: FragmentActivity, private val latLangg: LatLangg) :
    FragmentStateAdapter(fragmentActivity){
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putDouble("latitude", latLangg.latitude)
        bundle.putDouble("longitude", latLangg.longitude)
        return when (position) {
            0 ->{
                val nearYou = NearYou()
                nearYou.arguments = bundle
                nearYou
            }
            1 -> {
                val toppick = Toppick()
                toppick.arguments = bundle
                toppick
            }
            2 -> {
                val popular = Popular()
                popular.arguments = bundle
                popular
            }
            3 -> {
                val lunch = Lunch()
                lunch.arguments = bundle
                lunch
            }
            4 -> {
                val coffee = Coffee()
                coffee.arguments = bundle
                coffee
            }
            else -> throw Resources.NotFoundException("Position Not Found")
        }
    }

}
