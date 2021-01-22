package com.example.bookingapp.models

import com.google.gson.annotations.SerializedName

data class Reservation(
    @SerializedName("reservationId") val id: Int,
    @SerializedName("tableId") val numberName: Int,
    @SerializedName("dateStartReservation") val dateStartReservation: Long? = null,
    @SerializedName("dateEndReservation") val dateEndReservation: Long? = null,
    @SerializedName("order") val order: Int? = null,
    @SerializedName("restaurantId") val restaurantId: Int,
)
