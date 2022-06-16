package com.example.testdb.utils

import android.content.Context
import android.content.SharedPreferences

object AppPreference {
    private const val USER_EMAIl = "userEmail"
    private const val USER_PASSWORD = "userPassword"
    private const val USER_INIT = "userInit"
    private const val PREFERENCE_NAME = "preferenceName"

    private lateinit var preferences: SharedPreferences

    fun getPreference(context: Context):  SharedPreferences{
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences
    }

    fun setInitUser(init: Boolean, email: String, pass: String){
        preferences.edit()
            .putBoolean(USER_INIT, init)
            .putString(USER_EMAIl,email)
            .putString(USER_PASSWORD, pass)
            .apply()
    }

    fun getInitUser(): Boolean{
        return preferences.getBoolean(USER_INIT, false)
    }

    fun getUserEmail(): String{
        return preferences.getString(USER_EMAIl,"").toString()
    }

    fun getUserPass(): String{
        return preferences.getString(USER_PASSWORD,"").toString()
    }
}