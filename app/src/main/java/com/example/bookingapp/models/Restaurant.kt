package com.example.bookingapp.models

import com.google.gson.annotations.SerializedName

data class Restaurant(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String, val description: String = "description",
    @SerializedName( "reservations") val  reservations: List<Reservation> = emptyList(),
    var tables: MutableList<Table> = mutableListOf()
)