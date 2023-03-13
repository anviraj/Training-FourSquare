package com.example.foursquare.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foursquare.R
import com.example.foursquare.dataclass.GetFavouritesResponse
import com.example.foursquare.fragments.*
import com.example.foursquare.interfaces.Communicator

class Container : AppCompatActivity(), Communicator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        if(supportFragmentManager.backStackEntryCount == 0) {
            val extras = intent.extras
            when (extras?.get("to")) {
                "Details" -> {
                    val details = Details()
                    val bundle = Bundle()
                    bundle.putString("id", extras.get("id").toString())
                    bundle.putString("distance", extras.get("distance").toString())
                    bundle.putBoolean("favourite", extras.get("favourite") as Boolean)
                    details.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.container, details)
                        .commit()
                }

                "Search" -> {
                    val search = Search()
                    val bundle = Bundle()
                    bundle.putDouble("latitude", extras.get("latitude") as Double)
                    bundle.putDouble("longitude", extras.get("longitude") as Double)
                    search.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.container, search)
                        .commit()
                }

                "Filter" -> {
                    val filter = Filter()
                    val bundle = Bundle()
                    bundle.putString("from", "Container")
                    bundle.putDouble("latitude", extras.get("latitude") as Double)
                    bundle.putDouble("longitude", extras.get("longitude") as Double)
                    filter.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.container, filter)
                        .commit()
                }

                "Favourites" -> {
                    val favourites = Favourites()
                    val bundle = Bundle()
                    bundle.putDouble("latitude", extras.get("latitude") as Double)
                    bundle.putDouble("longitude", extras.get("longitude") as Double)
                    favourites.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.container, favourites)
                        .commit()
                }

                "Feedback" -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, Feedback())
                        .commit()
                }

                "AboutUs" -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, AboutUs())
                        .commit()
                }

                "Login" -> supportFragmentManager.beginTransaction().replace(R.id.container, Login())
                    .commit()
            }
        }
    }

    override fun gotoHome() {
        onBackPressed()
    }

    override fun gotoHomeParticularTab(tab: Int) {
        val intent = Intent()
        intent.putExtra("tab", tab)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}