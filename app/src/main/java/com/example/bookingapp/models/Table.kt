package com.example.bookingapp.models

import com.google.gson.annotations.SerializedName

data class Table(
    @SerializedName("id") val id: Int = -1,
    @SerializedName("isFree") val isFree: Boolean = false,
    @SerializedName("numberName") val numberName: Int = -1,
    @SerializedName("numberOfSeats") val numberOfSeats: Int = -1,
    @SerializedName("isNearTheWindow") val isNearTheWindow: Boolean = false,
    @SerializedName("waiterId") val waiterId: Int = -1
)
