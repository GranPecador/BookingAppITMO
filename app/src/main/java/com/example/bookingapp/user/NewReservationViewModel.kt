package com.example.bookingapp.user

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingapp.UserInfoPreferencesRepository
import com.example.bookingapp.models.NewReservation
import com.example.bookingapp.net.NetClient
import com.example.bookingapp.user.ui.restaurants.RestaurantCardAdapterRecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class NewReservationViewModel : ViewModel() {

    private var restaurantId: Int = -1
    private var tableId: Int = -1
    private val restaurants = UserViewModel.restaurants
    val adapterRest = RestaurantCardAdapterRecyclerView(restaurants)


    fun setSelectedRestaurantId(idRestaurant: Int) {
        restaurantId = idRestaurant
    }

    fun getTableForRecycler(idRestaurant: Int, applicationContext: Context) {
        viewModelScope.launch {
            val response = NetClient.instance.getTablesByRestaurant(idRestaurant)
            if (response.isSuccessful) {
                val list = response.body() ?: emptyList()
                if (!list.isNullOrEmpty()) {
                    adapterRest.addTables(list)
                } else {
                    Toast.makeText(applicationContext, "Нет доступных столиков", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun getRestaurantsFromServer(context: Context) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    NetClient.instance.getRestaurants()
                }
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        adapterRest.addItems(it)
                    }
                } else {
                    Toast.makeText(
                        context,
                        "${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: IOException) {
                Toast.makeText(
                    context,
                    "Не получиось загрузить рестораны",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun setSelectedTableId(idTable: Int) {
        tableId = idTable
    }

    fun createReservation(context: Context) {
        viewModelScope.launch {
            val userId =
                UserInfoPreferencesRepository.getInstance(context).userInfoPreferencesFlow.first().id
            try {
                val response = withContext(Dispatchers.IO) {
                    NetClient.instance.postReserve(
                        userId,
                        NewReservation(-1, userId, tableId, restaurantId)
                    )
                }
                if (response.isSuccessful) {
                    Toast.makeText(context, "Столик зарезервирован", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        context,
                        "Не получиось создать резервацию",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: IOException) {
                Toast.makeText(
                    context,
                    "Не получиось подключиться к серверу",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}