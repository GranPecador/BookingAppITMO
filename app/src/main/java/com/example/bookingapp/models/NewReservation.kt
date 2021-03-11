package com.example.bookingapp.models

import com.google.gson.annotations.SerializedName

data class NewReservation(
    @SerializedName("reservationId") val id: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("tableId") val numberName: Int,
    @SerializedName("restaurantId") val restaurantId: Int,
    @SerializedName("reservationStartTime") val start: Long,
    @SerializedName("reservationEndTime") val end: Long
)
