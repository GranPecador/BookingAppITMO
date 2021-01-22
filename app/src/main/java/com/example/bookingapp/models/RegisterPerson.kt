package com.example.bookingapp.models

import com.google.gson.annotations.SerializedName

data class RegisterPerson(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String,
    @SerializedName("fullname") val name: String,
    @SerializedName("telephone") val phone: String,
    @SerializedName("role") val role:String = "USER"
)