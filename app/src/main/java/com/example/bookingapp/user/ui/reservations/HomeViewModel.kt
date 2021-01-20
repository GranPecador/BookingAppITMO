package com.example.bookingapp.user.ui.reservations

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingapp.admin.ui.employees.EmployeesAdapterRecyclerView
import com.example.bookingapp.admin.ui.reservations.ReservationsAdapterRecyclerView
import com.example.bookingapp.models.Reservation
import com.example.bookingapp.net.NetClient
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val listReservations = mutableListOf<Reservation>(
        Reservation(0, 5, 555, 9999, 5, "Restaurant1" ),
        Reservation(1, 8, 5555, 99799, 6, "Restaurant2" ),
        Reservation(2, 5, 55555, 999944, null , "Restaurant3" )
    )

    private val _reservations : MutableLiveData<MutableList<Reservation>> = MutableLiveData(listReservations)
    val reservations: LiveData<MutableList<Reservation>> = _reservations

    val reservationAdapter = ReservationsAdapterRecyclerView(listReservations)

    private fun getReservationsFromServer(context: Context) {
        viewModelScope.launch {
            val response = NetClient().instance.getReservationsByAdmin(0)
            if (response.isSuccessful) {
                response.body()?.let { reservationAdapter.addItems(it) }
            }
        }
    }
}