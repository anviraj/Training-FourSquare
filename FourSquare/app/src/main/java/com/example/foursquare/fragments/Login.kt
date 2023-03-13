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
import com.example.foursquare.api.RetrofitClient
import com.example.foursquare.databinding.FragmentLoginBinding
import com.example.foursquare.dataclass.AddFavouriteResponse
import com.example.foursquare.dataclass.LoginResponse
import com.example.foursquare.dataclass.TokenManager
import com.example.foursquare.dataclass.User
import com.example.foursquare.interfaces.Communicator
import com.example.foursquare.storage.SharedPreferencesManager
import com.example.foursquare.viewmodels.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//@AndroidEntryPoint
class Login : Fragment() {

    private lateinit var loginBinding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    private lateinit var communicator: Communicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        SharedPreferencesManager.getInstance(loginBinding.root.context).saveToken(
            TokenManager(null, true)
        )

        communicator = activity as Communicator
        //initViewModel()
        loginBinding.apply {

            skipTv.setOnClickListener {
                communicator.gotoHome()
            }

            forgotPasswordTv.setOnClickListener {
                val email = emailEt.text.toString().trim()

                if (email.isEmpty()) {
                    emailEt.error = "Email required"
                    emailEt.requestFocus()
                    return@setOnClickListener
                } else emailEt.error = null

                val request = mapOf(
                    "email" to email,
                )

                initViewModel1(email)
                emailExists(request)

            }

            createAccountTv.setOnClickListener {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, Register())?.addToBackStack(null)?.commit()
            }

            loginBnTv.setOnClickListener {
                val email = emailEt.text.toString().trim()
                val password = passwordEt.text.toString().trim()


                if (email.isEmpty()) {
                    emailEt.error = "Email required"
                    emailEt.requestFocus()
                    return@setOnClickListener
                } else emailEt.error = null

                if (password.isEmpty()) {
                    passwordEt.error = "Password required"
                    passwordEt.requestFocus()
                    return@setOnClickListener
                } else passwordEt.error = null
                val user = User(email, password)

                login(user)
            }
        }


        return loginBinding.root
    }


    private fun login(user: User) {
        RetrofitClient.instance.login(user)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    val it = response.body()
                    if (it == null) {
                        Toast.makeText(this@Login.context, "Failed to log in", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this@Login.context, it.message, Toast.LENGTH_SHORT)
                            .show()

                        SharedPreferencesManager.getInstance(loginBinding.root.context).saveToken(
                            TokenManager("BEARER " + it.access_token, true)
                        )
                        communicator.gotoHome()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                }

            })

    }

    private fun initViewModel1(email: String) {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        viewModel.emailExistsObserver()
            .observe(viewLifecycleOwner, Observer<AddFavouriteResponse?> {

                if (it == null) {
                    //Toast.makeText(this@Register.context, "cannot send otp", Toast.LENGTH_SHORT).show()
                } else {

                    if (it.message.equals("email not registered", false)) {
                        Toast.makeText(this@Login.context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    } else {

                        val request = mapOf(
                            "email" to email,
                        )

                        getOtp(request)
                    }

                }
            })
    }

    private fun emailExists(request: Map<String, String>) {
        viewModel.emailExists(request)
    }


    private fun getOtp(request: Map<String, String>) {
        RetrofitClient.instance.getOtp(request)
            .enqueue(object : Callback<AddFavouriteResponse> {
                override fun onResponse(
                    call: Call<AddFavouriteResponse>,
                    response: Response<AddFavouriteResponse>
                ) {
                    val it = response.body()
                    if (it == null) {
                        Toast.makeText(this@Login.context, "cannot send otp", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this@Login.context, it.message, Toast.LENGTH_SHORT)
                            .show()

                        val forgotPassword = ForgotPassword()
                        val bundle = Bundle()
                        bundle.putString("from", "Login")

                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.container, forgotPassword)?.addToBackStack(null)
                            ?.commit()
                    }
                }

                override fun onFailure(call: Call<AddFavouriteResponse>, t: Throwable) {

                }

            })

    }

}