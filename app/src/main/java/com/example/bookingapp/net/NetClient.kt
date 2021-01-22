package com.example.bookingapp.net

import androidx.constraintlayout.solver.state.State
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object NetClient {
    private val url = "https://booking-app-2020.herokuapp.com/"

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

class AuthenticationInterceptor(private val authToken: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val builder: Request.Builder = original.newBuilder()
            .header("Authorization", authToken)
        val request: Request = builder.build()
        return chain.proceed(request)
    }
}