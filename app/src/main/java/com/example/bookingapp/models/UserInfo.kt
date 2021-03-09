package com.example.bookingapp.models

import com.example.bookingapp.ROLE
import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String,
    @SerializedName("fullname") val name: String,
    @SerializedName("telephone") val phone: String,
    @SerializedName("role") val role: ROLE = ROLE.USER,
    @SerializedName("restaurantId") val restaurantId: Int = -1
)
