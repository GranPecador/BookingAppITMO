package com.example.bookingapp.models

import com.google.gson.annotations.SerializedName

data class AuthHeader(@SerializedName("Authorization") val auth: String)