package com.example.bookingapp.user.ui.reservations

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingapp.CONST
import com.example.bookingapp.admin.ui.employees.EmployeesAdapterRecyclerView
import com.example.bookingapp.admin.ui.reservations.ReservationsAdapterRecyclerView
import com.example.bookingapp.models.Reservation
import com.example.bookingapp.net.NetClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private val listReservations = mutableListOf<Reservation>(
        Reservation(0, 5, 555, 9999, 5, 2),
        Reservation(1, 8, 5555, 99799, 6, 4),
        Reservation(2, 5, 55555, 999944, null, 5)
    )

    private val _reservations: MutableLiveData<MutableList<Reservation>> =
        MutableLiveData(listReservations)
    val reservations: LiveData<MutableList<Reservation>> = _reservations

    val reservationAdapter = ReservationsAdapterRecyclerView(listReservations)

    fun getReservationsFromServer(context: Context) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                NetClient.instance.getReservationsByUser(CONST.ID_ROLE_USER)
            }
            if (response.isSuccessful) {
                response.body()?.let { reservationAdapter.addItems(it) }
            }
        }
    }
}
