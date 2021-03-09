package com.example.bookingapp.models

import com.example.bookingapp.ROLE
import com.google.gson.annotations.SerializedName


data class Employee (@SerializedName("fullname") val name: String,
                     @SerializedName("id") val id: Int,
                     @SerializedName("role") val role : ROLE = ROLE.USER
)
