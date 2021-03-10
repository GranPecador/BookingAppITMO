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

    @GET("/users/profile")
    suspend fun getUserInfo(@Header("Authorization") auth: String): Response<UserInfo>

    @GET("/users/{userId}/reservations")
    suspend fun getReservationsByUser(@Path("userId") userId: Int): Response<List<Reservation>>

    @POST("/users/{userId}/reserve")
    suspend fun postReserve(
        @Path("userId") userId: Int,
        @Body newReservation: NewReservation
    ): Response<Void>

    @GET("/users/{userId}/reservations/{reservationId}/cancel")
    suspend fun getCancelReservation(
        @Path("userId") userId: Int,
        @Path("reservationId") reservationId: Int
    ): Response<Void>

    @GET("/restaurants")
    suspend fun getRestaurants(): Response<List<Restaurant>>

    @GET("/restaurants/{restaurantId}")
    suspend fun getReservationsByRestaurant(@Path("restaurantId") restaurantId: Int): Response<Restaurant>

    @GET("/restaurants/{restaurantId}/{userId}/tables")
    suspend fun getTablesByEmployee(
        @Path("restaurantId") restaurantId: Int,
        @Path("userId") userId: Int
    ): Response<List<Table>>

    @GET("/restaurants/{restaurantId}/tables")
    suspend fun getTablesByRestaurant(
        @Path("restaurantId") restaurantId: Int,
    ): Response<List<Table>>

    @PUT("/restaurants/{restaurantId}/tables/changeStatus")
    suspend fun putNewStatusOfTable(
        @Path("restaurantId") restaurantId: Int, @Query("isFree") isFree: Boolean,
        @Query("tableId") tableId: Int
    ): Response<Table>

    @POST("/restaurants/{restaurantId}/tables/assign")
    suspend fun postUpdateAssociateTable(
        @Path("restaurantId") restaurantId: Int,
        @Query("tableId") tableId: Int, @Query("userId") userId: Int
    ): Response<Void>

    @GET("/restaurants/{restaurantId}/workers")
    suspend fun getEmployeesByRestaurant(@Path("restaurantId") restaurantId: Int): Response<List<Employee>>

    @POST("/restaurants/{restaurantId}/workers/add")
    suspend fun postAddEmployee(
        @Path("restaurantId") restaurantId: Int,
        @Body userInfo: UserInfo
    ): Response<Void>
}