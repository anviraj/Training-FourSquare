package com.example.foursquare.storage

import android.annotation.SuppressLint
import android.content.Context
import com.example.foursquare.dataclass.TokenManager

class SharedPreferencesManager private constructor(private val mCtx: Context) {

    val isLoggedIn: Boolean
        get() {
            val sharedPreferences =
                mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("token", null).toString() != "null"
        }

    val isLogInShown: Boolean
        get() {
            val sharedPreferences =
                mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("loginShown", false)
        }

    val tokenManager: TokenManager
        get() {
            val sharedPreferences =
                mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return TokenManager(
                sharedPreferences.getString("token", null).toString(),
                sharedPreferences.getBoolean("loginShown", false)
            )
        }


    fun saveToken(tokenManager: TokenManager) {

        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("token", tokenManager.token)
        editor.putBoolean("loginShown", tokenManager.loginShown)

        editor.apply()

    }

    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private const val SHARED_PREF_NAME = "my_shared_pref"

        @SuppressLint("StaticFieldLeak")
        private var mInstance: SharedPreferencesManager? = null

        @Synchronized
        fun getInstance(mCtx: Context): SharedPreferencesManager {
            if (mInstance == null) {
                mInstance = SharedPreferencesManager(mCtx)
            }
            return mInstance as SharedPreferencesManager
        }
    }

}