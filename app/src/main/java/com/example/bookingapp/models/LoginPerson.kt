package com.example.bookingapp.models

import com.google.gson.annotations.SerializedName

data class LoginPerson(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String,
)
