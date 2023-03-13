package com.example.foursquare.fragments

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.foursquare.R
import com.example.foursquare.databinding.FragmentRegisterBinding
import com.example.foursquare.dataclass.AddFavouriteResponse
import com.example.foursquare.interfaces.Communicator
import com.example.foursquare.viewmodels.LoginViewModel
import java.util.regex.Pattern

class Register : Fragment() {
    private lateinit var registerBinding: FragmentRegisterBinding
    private lateinit var viewModel: LoginViewModel

    private lateinit var communicator: Communicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registerBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        registerBinding.apply {
            loginBnTv.setOnClickListener {
                val email = emailEt.text.toString().trim()
                val password = passwordEt.text.toString().trim()
                val mobile = mobileNumEt.text.toString().trim()
                val cpassword = cpasswordEt.text.toString().trim()

                if (email.isEmpty()) {
                    emailEt.error = "Email required"
                    emailEt.requestFocus()
                    return@setOnClickListener
                } else if (!validateEmail(email)) {
                    emailEt.error = "Invalid Email"
                    emailEt.requestFocus()
                    return@setOnClickListener
                } else emailEt.error = null

                if (mobile.isEmpty()) {
                    mobileNumEt.error = "Mobile Number required"
                    mobileNumEt.requestFocus()
                    return@setOnClickListener
                } else if (!validateMobile(mobile)) {
                    mobileNumEt.error = "Invalid Mobile Number"
                    mobileNumEt.requestFocus()
                    return@setOnClickListener
                } else mobileNumEt.error = null

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
                    cpasswordEt.error = "Confirm Password required"
                    cpasswordEt.requestFocus()
                    return@setOnClickListener
                } else cpasswordEt.error = null

                if (password == cpassword) {
                    cpasswordEt.error = null

                    val request = mapOf(
                        "email" to email,
                    )

                    initViewModel(request, mobile, password)
                    getOtp(request)

                } else {
                    cpasswordEt.error = "Confirm Password should be same as Password"
                    cpasswordEt.requestFocus()
                    return@setOnClickListener
                }
            }
        }

        return registerBinding.root
    }

    private fun validateEmail(email: String): Boolean {
        val pattern =
            Patterns.EMAIL_ADDRESS
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun validateMobile(mobile: String): Boolean {
        val pattern = Pattern.compile("[6-9][0-9]{9}")
        val matcher = pattern.matcher(mobile)
        return matcher.matches()
    }

    private fun initViewModel(
        request: Map<String, String>,
        mobile: String,
        password: String
    ) {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.otpObserver().observe(viewLifecycleOwner, Observer<AddFavouriteResponse?> {

            if (it == null) {
                Toast.makeText(this@Register.context, "cannot send otp", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this@Register.context, it.message, Toast.LENGTH_SHORT)
                    .show()

                val forgotPassword = ForgotPassword()
                val bundle = Bundle()
                bundle.putString("email", request["email"])
                bundle.putString("phoneNumber", mobile)
                bundle.putString("password", password)
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, forgotPassword)?.commit()
            }
        })
    }

    private fun getOtp(request: Map<String, String>) {
        viewModel.getOtp(request)
    }


}