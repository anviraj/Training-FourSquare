package com.example.foursquare.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foursquare.R
import com.example.foursquare.activities.Container
import com.example.foursquare.dataclass.AddFavouriteResponse
import com.example.foursquare.dataclass.GetFavouritesResponse
import com.example.foursquare.dataclass.Id
import com.example.foursquare.fragments.Favourites
import com.example.foursquare.storage.SharedPreferencesManager
import com.example.foursquare.viewmodels.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import kotlin.math.roundToInt

class FavouritesRecyclerViewAdapter(
    private val activity: FragmentActivity?,
    private val getFavouritesResponse: GetFavouritesResponse,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val viewLifecycleOwner: LifecycleOwner,
    private val favourites: Favourites,
    private val recyclerview: RecyclerView,
) :
    RecyclerView.Adapter<FavouritesRecyclerViewAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var viewModel: LoginViewModel

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val placeImage: ImageView = itemView.findViewById(R.id.placeImage_iv)
        val placeName: TextView = itemView.findViewById(R.id.placeName_tv)
        val rating: TextView = itemView.findViewById(R.id.rating_tv)
        val ratings: LinearLayout = itemView.findViewById(R.id.rating_lyt)

        val description: TextView = itemView.findViewById(R.id.description_tv)
        val description1: TextView = itemView.findViewById(R.id.description1_tv)
        val favourite: ConstraintLayout = itemView.findViewById(R.id.favourite)
        val delete: ImageView = itemView.findViewById(R.id.delete_iv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourites_data_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = getFavouritesResponse[position]
        var url = data.placeImage

        if (!url.contains("https"))
            url = "${url.substring(0..3)}s${url.substring(4)}"

        loadImageFromWebUrl(url, holder.placeImage)
        val distance = ((data.distance.calculated / 1609) * 100).roundToInt() / 100.0
        holder.apply {
            placeName.text = data.placeName
            rating.text = ((data.rating * 10).roundToInt() / 10.0).toString()
            val s = when (data.placePriceRange) {
                1 -> "₹"
                2 -> "₹₹"
                3 -> "₹₹₹"
                else -> "₹₹₹₹"
            }

            description.text = "${data.placeCategory} $s \t${distance}km"
            description1.text = data.placeAddress

            delete.setOnClickListener {
                val id = Id(data.placeId)
                initViewModel()
                val sharedPreferencesManager = SharedPreferencesManager.getInstance(context)
                sharedPreferencesManager.tokenManager.token?.let { it1 -> addFavourite(it1, id) }

                getFavouritesResponse.removeAt(position)
                recyclerview.layoutManager =
                    LinearLayoutManager(context)
                recyclerview.adapter =
                    FavouritesRecyclerViewAdapter(
                        activity,
                        getFavouritesResponse,
                        lifecycleScope,
                        viewLifecycleOwner,
                        favourites,
                        recyclerview
                    )

                Toast.makeText(context, "favourite removed", Toast.LENGTH_SHORT).show()
            }
        }

        val hotelrating = ((data.rating * 10).roundToInt() / 10.0).toString()
        if(hotelrating >= 4.toString()) {
            holder.ratings.setBackgroundResource(R.drawable.custom_rating_green)
        } else if (hotelrating >= 2.toString()) {
            holder.ratings.setBackgroundResource(R.drawable.custom_rating_yellow)
        } else {
            holder.ratings.setBackgroundResource(R.drawable.custom_rating_orange)

        }


        holder.itemView.setOnClickListener {
            val intent = Intent(activity, Container::class.java)
            intent.putExtra("to", "Details")
            intent.putExtra("id", data.placeId)
            intent.putExtra("distance", distance)
            intent.putExtra("favourite", true)
            activity?.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return getFavouritesResponse.size
    }

    private fun loadImageFromWebUrl(url: String, placeImage: ImageView) {
        lifecycleScope.launch(Dispatchers.IO) {
            val image = try {
                val iStream = java.net.URL(url).content as InputStream
                Drawable.createFromStream(iStream, "placeImage")
            } catch (e: Exception) {
                null
            }

            image?.let {
                withContext(Dispatchers.Main) {
                    placeImage.background = it
                }
            }
        }
    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(favourites)[LoginViewModel::class.java]
        viewModel.addFavouriteObserver()
            .observe(viewLifecycleOwner, Observer<AddFavouriteResponse?> {

                if (it == null) {
                    Toast.makeText(context, "Not added to favourite", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("responseeeeeeeeee", it.toString())
                }
            })
    }

    private fun addFavourite(token: String, id: Id) {
        viewModel.addFavourite(token, id)
    }

}
