package com.example.bookingapp.waiter

import android.content.Context
import androidx.lifecycle.*
import com.example.bookingapp.waiter.ui.reservations.ReservationsAdapterRecyclerView
import com.example.bookingapp.models.Reservation
import com.example.bookingapp.net.NetClient
import kotlinx.coroutines.launch

class WaiterViewModel: ViewModel() {

    private val listReservations = mutableListOf<Reservation>(
        Reservation(0, 5, 555, 9999, 5),
        Reservation(1, 8, 5555, 99799, 6),
        Reservation(2, 5, 55555, 999944, null )
    )
    private val listTable = mutableListOf(5, 10)

    private val _reservations : MutableLiveData<MutableList<Reservation>> = MutableLiveData(listReservations)
    val reservations: LiveData<MutableList<Reservation>> = _reservations
    private val _tables : MutableLiveData<MutableList<Int>> = MutableLiveData(listTable)
    val tables: LiveData<MutableList<Int>> = _tables

    val reservationAdapter = ReservationsAdapterRecyclerView(listReservations)

    fun updateData() {
    }

    fun getTables() = tables.value?.map { "Стол №${it}" } ?: emptyList()


    private fun getReservationsFromServer(context: Context) {
        viewModelScope.launch {
            val response = NetClient().instance.getReservationsByAdmin(0)
            if (response.isSuccessful) {
                response.body()?.let { reservationAdapter.addItems(it) }
            }
        }
    }
}