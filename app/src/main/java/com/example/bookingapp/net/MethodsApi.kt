package com.example.bookingapp.net

import com.example.bookingapp.models.*
import retrofit2.Response
import retrofit2.http.*

interface MethodsApi {

    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("/users/register")
    suspend fun postRegister(@Body registerPerson: RegisterPerson): Response<RegisteredPerson>

    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("/login")
    suspend fun postLogin(@Body loginPerson: LoginPerson): Response<Void>


    @GET("/admins/{adminId}/reservations")
    suspend fun getReservationsByAdmin(@Path("adminId") adminId: Int): Response<List<Reservation>>

    @GET("/users/{userId}/reservations")
    suspend fun getReservationsByUser(@Path("userId") userId: Int): Response<List<Reservation>>

    @POST("/users/{userId}/reserve")
    suspend fun postReserve(@Path("userId") userId: Int, @Body newReservation: NewReservation): Response<Void>
}
