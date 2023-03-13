package com.example.foursquare.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.foursquare.R
import com.example.foursquare.databinding.FragmentForgotPassword1Binding
import com.example.foursquare.databinding.FragmentNearYouBinding
import com.example.foursquare.viewmodels.LoginViewModel

class ForgotPassword1 : Fragment() {

    private lateinit var forgotPassword1Binding: FragmentForgotPassword1Binding
    private lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        forgotPassword1Binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password1, container, false)

        val email = arguments?.getString("email")
        forgotPassword1Binding.apply {
            val password = passwordEt.text.toString().trim()
            val cpassword = cPasswordEt.text.toString().trim()

            submitBnTv.setOnClickListener {
                if (password.isEmpty()) {
                    passwordEt.error = "Password required"
                    passwordEt.requestFocus()
                    return@setOnClickListener
                } else if (password.length < 6) {
                    passwordEt.error = "Password length should be atleast 6 characters"
                    passwordEt.requestFocus()
                    return@setOnClickListener
                } else passwordEt.error = null

                if (cpassword.isEmpty()) {
                    cPasswordEt.error = "Confirm Password required"
                    cPasswordEt.requestFocus()
                    return@setOnClickListener
                } else cPasswordEt.error = null

                if (password == cpassword) {
                    cPasswordEt.error = null

                    val request = mapOf(
                        "email" to email,
                    )

                } else {
                    cPasswordEt.error = "Confirm Password should be same as Password"
                    cPasswordEt.requestFocus()
                    return@setOnClickListener
                }
            }
        }

        return forgotPassword1Binding.root
    }

}