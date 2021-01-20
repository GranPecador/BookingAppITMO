package com.example.bookingapp.models

import com.google.gson.annotations.SerializedName

data class Reservation(
    @SerializedName("id") val id: Int,
    @SerializedName("numberName") val numberName: Int,
    @SerializedName("dateStartReservation") val dateStartReservation: Long,
    @SerializedName("dateEndReservation") val dateEndReservation: Long,
    @SerializedName("order") val order: Int?,
    @SerializedName("restaurant") val restaurant: String = "",
)
