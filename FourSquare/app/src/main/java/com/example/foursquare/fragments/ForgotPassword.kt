package com.example.foursquare.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.foursquare.R
import com.example.foursquare.databinding.FragmentForgotPasswordBinding
import com.example.foursquare.dataclass.AddFavouriteResponse
import com.example.foursquare.viewmodels.LoginViewModel

class ForgotPassword : Fragment() {


    private lateinit var forgotPasswordBinding: FragmentForgotPasswordBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        forgotPasswordBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false)

        val email = arguments?.getString("email").toString()
        val phoneNumber = arguments?.getString("phoneNumber").toString()
        val password = arguments?.getString("password").toString()

        val registerRequest = mapOf<String, String>(
            "email" to email,
            "phoneNumber" to phoneNumber,
            "password" to password
        )

        forgotPasswordBinding.apply {
            getInBnTv.setOnClickListener {
                val otpRequest = mapOf<String, String>(
                    "otp" to otpEt.text.toString()
                )

                initViewModel1(registerRequest)
                verifyOtp(otpRequest)
            }
        }

        return forgotPasswordBinding.root
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.registerObserver().observe(viewLifecycleOwner, Observer<AddFavouriteResponse?> {

            if (it == null) {
                Toast.makeText(
                    this@ForgotPassword.context,
                    "user already exists!",
                    Toast.LENGTH_SHORT
                )
                    .show()

                activity?.supportFragmentManager?.popBackStack()
            } else {
                Toast.makeText(this@ForgotPassword.context, it.message, Toast.LENGTH_SHORT)
                    .show()

                activity?.supportFragmentManager?.popBackStack()
            }
        })
    }

    private fun register(request: Map<String, String>) {
        viewModel.register(request)
    }

    private fun initViewModel1(registerRequest: Map<String, String>) {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.verifyOtpObserver().observe(viewLifecycleOwner, Observer<Map<String, Boolean>?> {

            if (it == null) {

            } else {
                if (it["message"] == true) {
                    if (arguments?.getString("from") == "Login") {
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.container, ForgotPassword1())?.commit()
                    } else {
                        initViewModel()
                        register(registerRequest)
                    }
                } else Toast.makeText(
                    this@ForgotPassword.context,
                    "Please enter correct otp",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    private fun verifyOtp(otpRequest: Map<String, String>) {
        viewModel.verifyOtp(otpRequest)
    }
}