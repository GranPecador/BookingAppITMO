package com.example.bookingapp.models

import com.google.gson.annotations.SerializedName

data class Reservations(@SerializedName("reservations") val reservations: List<Reservation>)