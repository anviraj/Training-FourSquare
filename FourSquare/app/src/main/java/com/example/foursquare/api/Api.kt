package com.example.foursquare.api

import com.example.foursquare.dataclass.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface Api {

    @Headers("Content-Type:application/json")
    @POST("login")
    fun login(
        @Body user: User
    ): Call<LoginResponse>

    @Headers("Content-Type:application/json")
    @POST("getNearPlace")
    fun nearYou(
        @Body latLng: LatLangg
    ): Call<NearYouResponse>

    @Headers("Content-Type:application/json")
    @POST("getTopPlace")
    fun topPick(
        @Body latLng: LatLangg
    ): Call<NearYouResponse>

    @Headers("Content-Type:application/json")
    @POST("getPopularPlace")
    fun popular(
        @Body latLng: LatLangg
    ): Call<NearYouResponse>

    @Headers("Content-Type:application/json")
    @POST("getRestaurants")
    fun lunch(
        @Body latLng: LatLangg
    ): Call<NearYouResponse>

    @Headers("Content-Type:application/json")
    @POST("getCafe")
    fun coffee(
        @Body latLng: LatLangg
    ): Call<NearYouResponse>

    @Headers("Content-Type:application/json")
    @POST("getParticularPlace")
    fun getParticularPlace(@Body id: Map<String, String>): Call<GetParticularPlaceResponse>

    @Headers("Content-Type:application/json")
    @POST("getReview")
    fun getAllReview(@Body id: Id): Call<GetAllReviewResponse>

    @Headers("Content-Type:application/json")
    @POST("getNearCity")
    fun getNearByPlaces(@Body latLng: LatLangg): Call<GetNearByPlacesResponse>

    @Headers("Content-Type:application/json")
    @POST("searchPlace")
    fun getSearchPlaces(@Body data: GetSearchPlacesRequest): Call<GetSearchPlacesResponse>

    @Headers("Content-Type:application/json")
    @POST("getReviewImage")
    fun getReviewImage(@Body id: Id): Call<GetReviewImageResponse>

    @Headers("Content-Type:application/json")
    @POST("filterSearch")
    fun getFilterSearch(@Body preferences: MutableMap<String, Any>): Call<GetFilterSearchResponse>

    @Headers("Content-Type:application/json")
    @POST("getProfile")
    fun getProfile(@Header("Authorization") token: String): Call<GetProfileResponse>

    @Headers("Content-Type:application/json")
    @POST("addFavourite")
    fun addFavourite(
        @Header("Authorization") token: String,
        @Body id: Id
    ): Call<AddFavouriteResponse>

    @Headers("Content-Type:application/json")
    @POST("searchFavourite")
    fun getFavourites(
        @Header("Authorization") token: String,
        @Body getFavouritesRequest: GetFavouritesRequest
    ): Call<GetFavouritesResponse>

    @Headers("Content-Type:application/json")
    @POST("addRating")
    fun addRating(
        @Header("Authorization") token: String,
        @Body addRatingRequest: MutableMap<String, Any>
    ): Call<AddFavouriteResponse>

    @Headers("Content-Type:application/json")
    @POST("addFeedback")
    fun getFeedback(
        @Header("Authorization") token: String,
        @Body getFeedbackRequest: GetFeedbackRequest
    ): Call<AddFavouriteResponse>

    @GET("getAboutUs")
    fun getAboutUs(): Call<GetAboutUsResponse>

    @Headers("Content-Type:application/json")
    @POST("addReview")
    fun addReviewText(
        @Header("Authorization") token: String,
        @Body request: Map<String, String>
    ): Call<AddFavouriteResponse>

    @Multipart
    @POST("addReviewImage")
    fun addReviewImages(
        @Header("Authorization") token: String,
        @Part("_id") id: RequestBody,
        @Part("image") imagesList: MutableList<ImageList>
    ): Call<AddFavouriteResponse>

    @Headers("Content-Type:application/json")
    @POST("register")
    fun register(
        @Body request: Map<String, String>
    ): Call<AddFavouriteResponse>

    @Headers("Content-Type:application/json")
    @POST("sendOtp")
    fun getOtp(
        @Body request: Map<String, String>
    ): Call<AddFavouriteResponse>

    @Headers("Content-Type:application/json")
    @POST("emailExists")
    fun emailExists(
        @Body request: Map<String, String>
    ): Call<AddFavouriteResponse>

    @Headers("Content-Type:application/json")
    @POST("verifyOtp")
    fun verifyOtp(
        @Body request: Map<String, String>
    ): Call<Map<String, Boolean>>

}