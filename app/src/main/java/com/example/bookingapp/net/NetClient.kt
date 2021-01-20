package com.example.bookingapp.net

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetClient {
    private val url = "http"

    private val logging = HttpLoggingInterceptor()

    private val client = OkHttpClient.Builder()

    val instance: MethodsApi by lazy {

        logging.level = HttpLoggingInterceptor.Level.BODY
        client.addInterceptor(logging)

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()

        retrofit.create(MethodsApi::class.java)
    }
}