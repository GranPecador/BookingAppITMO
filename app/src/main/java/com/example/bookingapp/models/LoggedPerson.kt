package com.example.bookingapp.models

import com.google.gson.annotations.SerializedName
import retrofit2.http.Header

data class LoggedPerson(
    @Header("Authorization") val authHeader: String = "",
    @SerializedName("Authorization") val auth: String = ""
)

