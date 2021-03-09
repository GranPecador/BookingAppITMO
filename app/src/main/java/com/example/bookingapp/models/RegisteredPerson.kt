package com.example.bookingapp.models

import com.example.bookingapp.ROLE
import com.google.gson.annotations.SerializedName

data class RegisteredPerson(@SerializedName("id") val id: Int,
                            @SerializedName("roleType") val role: ROLE
)