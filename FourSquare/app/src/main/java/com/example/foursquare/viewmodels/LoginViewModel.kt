package com.example.foursquare.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foursquare.api.RetrofitClient
import com.example.foursquare.dataclass.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    var nearYouLiveData: MutableLiveData<NearYouResponse?> = MutableLiveData()
    var reviewLiveData: MutableLiveData<GetAllReviewResponse?> = MutableLiveData()
    var reviewImageLiveData: MutableLiveData<GetReviewImageResponse?> = MutableLiveData()
    var addFavouriteLiveData: MutableLiveData<AddFavouriteResponse?> = MutableLiveData()
    var getFavouriteLiveData: MutableLiveData<GetFavouritesResponse?> = MutableLiveData()
    var addFeedbackLiveData: MutableLiveData<AddFavouriteResponse?> = MutableLiveData()
    var registerLiveData: MutableLiveData<AddFavouriteResponse?> = MutableLiveData()
    var otpLiveData: MutableLiveData<AddFavouriteResponse?> = MutableLiveData()
    var emailExistsLiveData: MutableLiveData<AddFavouriteResponse?> = MutableLiveData()
    var verifyOtpLiveData: MutableLiveData<Map<String, Boolean>?> = MutableLiveData()


    fun nearYou(latLangg: LatLangg) {
        RetrofitClient.instance.nearYou(latLangg)
            .enqueue(object : Callback<NearYouResponse> {
                override fun onResponse(
                    call: Call<NearYouResponse>,
                    response: Response<NearYouResponse>
                ) {
                    nearYouLiveData.postValue(
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            null
                        }
                    )
                }

                override fun onFailure(call: Call<NearYouResponse>, t: Throwable) {
                    nearYouLiveData.postValue(null)
                }

            })
    }

    fun getReviewObserver(): MutableLiveData<GetAllReviewResponse?> {
        return reviewLiveData
    }


    fun getReviewImageObserver(): MutableLiveData<GetReviewImageResponse?> {
        return reviewImageLiveData
    }

    fun getReviewImage(id: Id) {
        RetrofitClient.instance.getReviewImage(id)
            .enqueue(object : Callback<GetReviewImageResponse> {
                override fun onResponse(
                    call: Call<GetReviewImageResponse>,
                    response: Response<GetReviewImageResponse>
                ) {
                    reviewImageLiveData.postValue(
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            null
                        }
                    )
                }

                override fun onFailure(call: Call<GetReviewImageResponse>, t: Throwable) {
                    reviewImageLiveData.postValue(null)
                }

            })

    }


    fun addFavouriteObserver(): MutableLiveData<AddFavouriteResponse?> {
        return addFavouriteLiveData
    }

    fun addFavourite(token: String, id: Id) {
        RetrofitClient.instance.addFavourite(token, id)
            .enqueue(object : Callback<AddFavouriteResponse> {
                override fun onResponse(
                    call: Call<AddFavouriteResponse>,
                    response: Response<AddFavouriteResponse>
                ) {
                    addFavouriteLiveData.postValue(
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            null
                        }
                    )
                }

                override fun onFailure(call: Call<AddFavouriteResponse>, t: Throwable) {
                    addFavouriteLiveData.postValue(null)
                }

            })

    }

    fun getFavouritesObserver(): MutableLiveData<GetFavouritesResponse?> {
        return getFavouriteLiveData
    }

    fun getFavourites(token: String, getFavouritesRequest: GetFavouritesRequest) {
        RetrofitClient.instance.getFavourites(token, getFavouritesRequest)
            .enqueue(object : Callback<GetFavouritesResponse> {
                override fun onResponse(
                    call: Call<GetFavouritesResponse>,
                    response: Response<GetFavouritesResponse>
                ) {
                    getFavouriteLiveData.postValue(
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            null
                        }
                    )
                }

                override fun onFailure(call: Call<GetFavouritesResponse>, t: Throwable) {
                    getFavouriteLiveData.postValue(null)
                }

            })

    }


    fun getFeedbackObserver(): MutableLiveData<AddFavouriteResponse?> {
        return addFeedbackLiveData
    }

    fun getFeedback(token: String, getFeedbackRequest: GetFeedbackRequest) {
        RetrofitClient.instance.getFeedback(token, getFeedbackRequest)
            .enqueue(object : Callback<AddFavouriteResponse> {
                override fun onResponse(
                    call: Call<AddFavouriteResponse>,
                    response: Response<AddFavouriteResponse>
                ) {
                    addFeedbackLiveData.postValue(
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            null
                        }
                    )
                }

                override fun onFailure(call: Call<AddFavouriteResponse>, t: Throwable) {
                    addFeedbackLiveData.postValue(null)
                }

            })

    }


    fun registerObserver(): MutableLiveData<AddFavouriteResponse?> {
        return registerLiveData
    }

    fun register(request: Map<String, String>) {
        RetrofitClient.instance.register(request)
            .enqueue(object : Callback<AddFavouriteResponse> {
                override fun onResponse(
                    call: Call<AddFavouriteResponse>,
                    response: Response<AddFavouriteResponse>
                ) {
                    registerLiveData.postValue(
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            null
                        }
                    )
                }

                override fun onFailure(call: Call<AddFavouriteResponse>, t: Throwable) {
                    registerLiveData.postValue(null)
                }

            })

    }

    fun otpObserver(): MutableLiveData<AddFavouriteResponse?> {
        return otpLiveData
    }

    fun getOtp(request: Map<String, String>) {
        RetrofitClient.instance.getOtp(request)
            .enqueue(object : Callback<AddFavouriteResponse> {
                override fun onResponse(
                    call: Call<AddFavouriteResponse>,
                    response: Response<AddFavouriteResponse>
                ) {
                    otpLiveData.postValue(
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            null
                        }
                    )
                }

                override fun onFailure(call: Call<AddFavouriteResponse>, t: Throwable) {
                    otpLiveData.postValue(null)
                }

            })

    }

    fun emailExistsObserver(): MutableLiveData<AddFavouriteResponse?> {
        return emailExistsLiveData
    }

    fun emailExists(request: Map<String, String>) {
        RetrofitClient.instance.emailExists(request)
            .enqueue(object : Callback<AddFavouriteResponse> {
                override fun onResponse(
                    call: Call<AddFavouriteResponse>,
                    response: Response<AddFavouriteResponse>
                ) {
                    emailExistsLiveData.postValue(
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            null
                        }
                    )
                }

                override fun onFailure(call: Call<AddFavouriteResponse>, t: Throwable) {
                    emailExistsLiveData.postValue(null)
                }

            })

    }

    fun verifyOtpObserver(): MutableLiveData<Map<String, Boolean>?> {
        return verifyOtpLiveData
    }

    fun verifyOtp(request: Map<String, String>) {
        RetrofitClient.instance.verifyOtp(request)
            .enqueue(object : Callback<Map<String, Boolean>> {
                override fun onResponse(
                    call: Call<Map<String, Boolean>>,
                    response: Response<Map<String, Boolean>>
                ) {
                    verifyOtpLiveData.postValue(
                        if (response.isSuccessful) {
                            response.body()
                        } else {
                            null
                        }
                    )
                }

                override fun onFailure(call: Call<Map<String, Boolean>>, t: Throwable) {
                    verifyOtpLiveData.postValue(null)
                }

            })

    }
}