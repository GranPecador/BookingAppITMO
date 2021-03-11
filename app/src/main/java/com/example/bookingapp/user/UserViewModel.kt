package com.example.bookingapp.user

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingapp.UserInfoPreferencesRepository
import com.example.bookingapp.models.Reservation
import com.example.bookingapp.models.Restaurant
import com.example.bookingapp.net.NetClient
import com.example.bookingapp.user.ui.reservations.ReservationsAdapterRecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class UserViewModel : ViewModel() {

    private val listReservations = mutableListOf<Reservation>(
        //Reservation(0, 5, 555, 9999, 5, 2),
       // Reservation(1, 8, 5555, 99799, 6, 4),
       // Reservation(2, 5, 55555, 999944, null, 5)
    )

    private val _reservations: MutableLiveData<MutableList<Reservation>> =
        MutableLiveData(listReservations)
    val reservations: LiveData<MutableList<Reservation>> = _reservations

    val reservationAdapter = ReservationsAdapterRecyclerView(listReservations)

    fun updateData(context: Context) {
        getRestaurantsFromServer()
        getReservationsFromServer(context)
    }

    private fun getReservationsFromServer(context: Context) {
        viewModelScope.launch {
            val userId =
                UserInfoPreferencesRepository.getInstance(context).userInfoPreferencesFlow.first().id
            try {
                val response = withContext(Dispatchers.IO) {
                    NetClient.instance.getReservationsByUser(userId)
                }
                if (response.isSuccessful) {
                    response.body()?.let { reservationAdapter.addItems(it) }
                } else {
                    Toast.makeText(
                        context,
                        "Не получилось загрузить резервации.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: IOException) {
                Toast.makeText(
                    context,
                    "Не получилось подключиться к серверу. Обновите страницу.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun getRestaurantsFromServer() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    NetClient.instance.getRestaurants()
                }
                if (response.isSuccessful) {
                    restaurants.clear()
                    val body = response.body()
                    body?.let{
                        restaurants.addAll(it)
                        if (_reservations.value?.isNotEmpty() == true) {
                            reservationAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }catch (e:IOException){
            }
        }
    }

    companion object {
        val restaurants = mutableListOf<Restaurant>()
    }
}
