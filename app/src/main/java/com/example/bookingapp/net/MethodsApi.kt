package com.example.bookingapp.net

import com.example.bookingapp.models.Reservation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MethodsApi {

    @GET("/admins/{adminId}/reservations")
    suspend fun getReservationsByAdmin(@Path("adminId") adminId: Int): Response<List<Reservation>>


}
