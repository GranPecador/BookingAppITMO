package com.example.bookingapp.user

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingapp.UserInfoPreferencesRepository
import com.example.bookingapp.models.NewReservation
import com.example.bookingapp.models.Restaurant
import com.example.bookingapp.net.NetClient
import com.example.bookingapp.user.ui.restaurants.RestaurantCardAdapterRecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.sql.Date
import java.util.*

class NewReservationViewModel : ViewModel() {

    private var restaurantId: Int = -1
    private var tableId: Int = -1
    private val restaurants = mutableListOf<Restaurant>()
    val adapterRest = RestaurantCardAdapterRecyclerView(getRestaurants())

    var currentStart = -1
    var currentEnd = -1
    var currentDate = Date(0)
    var dayOfMonth = -1

    fun setSelectedRestaurantId(idRestaurant: Int) {
        restaurantId = idRestaurant
    }

    fun getTableForRecycler(idRestaurant: Int, applicationContext: Context) {
        viewModelScope.launch {
            val response = NetClient.instance.getTablesByRestaurant(idRestaurant)
            try {
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    if (!list.isNullOrEmpty()) {
                        adapterRest.addTables(list)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Нет доступных столиков",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            } catch (e: IOException) {
                Toast.makeText(
                    applicationContext,
                    "Не получиось загрузить столики. Нажмите еще раз.",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun getRestaurants(): MutableList<Restaurant>{
        if (restaurants.isEmpty()) {
            restaurants.addAll(UserViewModel.restaurants)
        }
        return restaurants
        /*viewModelScope.launch(Dispatchers.Main) {
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
        }*/
    }

    fun setSelectedTableId(idTable: Int) {
        tableId = idTable
    }

    fun createReservation(context: Context) {
        if (currentDate.time <= Date(0).time){
            Log.e("date", currentDate.time.toString())
            Log.e("date", Calendar.getInstance().timeInMillis.toString())
            Toast.makeText(context, "Выбирете дату брони", Toast.LENGTH_LONG).show()
            return
        }
        if ((currentStart >= currentEnd) || (currentStart == -1) || (currentEnd == -1)) {
            Toast.makeText(context, "Выбирете время брони", Toast.LENGTH_LONG).show()
            return
        }
        //if (Build.VERSION.SDK_INT>25) {
            //val dateTime = LocalDateTime.of(currentDate.year, currentDate.month, currentDate.day, currentStart, 0)
        //} else {

        val startDate = Date(currentDate.year, currentDate.month, dayOfMonth, currentStart, 0)
        val endDate = Date(currentDate.year, currentDate.month, dayOfMonth, currentEnd, 0)

        viewModelScope.launch {
            val userId =
                UserInfoPreferencesRepository.getInstance(context).userInfoPreferencesFlow.first().id
            try {
                val response = withContext(Dispatchers.IO) {
                    NetClient.instance.postReserve(
                        userId,
                        NewReservation(-1, userId, tableId, restaurantId, startDate.time, endDate.time)
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