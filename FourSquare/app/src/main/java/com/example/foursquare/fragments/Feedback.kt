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
import com.example.foursquare.R
import com.example.foursquare.databinding.FragmentFeedbackBinding
import com.example.foursquare.dataclass.AddFavouriteResponse
import com.example.foursquare.dataclass.GetFavouritesResponse
import com.example.foursquare.dataclass.GetFeedbackRequest
import com.example.foursquare.interfaces.Communicator
import com.example.foursquare.storage.SharedPreferencesManager
import com.example.foursquare.viewmodels.LoginViewModel

class Feedback : Fragment() {

    private lateinit var feedbackBinding: FragmentFeedbackBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        feedbackBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_feedback, container, false)

        feedbackBinding.backIv.setOnClickListener {
            val communicator = activity as Communicator
            communicator.gotoHome()
        }

        feedbackBinding.submitTv.setOnClickListener {
            val getFeedbackRequest =
                GetFeedbackRequest(feedbackBinding.feedbackTextInputEditText.text.toString())
            val token =
                SharedPreferencesManager.getInstance(feedbackBinding.root.context).tokenManager.token
            initViewModel()
            if (token != null) {
                getFeedback(token, getFeedbackRequest)
            }
        }

        return feedbackBinding.root
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.getFeedbackObserver()
            .observe(viewLifecycleOwner, Observer<AddFavouriteResponse?> {

                if (it == null) {
                } else {
                    Log.d("response", it.toString())
                    Toast.makeText(this@Feedback.context, it.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getFeedback(token: String, getFeedbackRequest: GetFeedbackRequest) {
        viewModel.getFeedback(token, getFeedbackRequest)
    }
}