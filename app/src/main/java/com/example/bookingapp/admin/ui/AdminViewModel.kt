package com.example.bookingapp.admin.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingapp.admin.ui.employees.EmployeesAdapterRecyclerView
import com.example.bookingapp.admin.ui.reservations.ReservationsAdapterRecyclerView
import com.example.bookingapp.models.Employee
import com.example.bookingapp.models.Reservation
import com.example.bookingapp.net.NetClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminViewModel: ViewModel() {


    private val listEmployees = mutableListOf(Employee("Иванов Иван Иванович", 1),
        Employee("Петров Петр Петрович", 2))
    private val listReservations = mutableListOf<Reservation>(
        Reservation(0, 5, 555, 9999, 5, 4),
        Reservation(1, 8, 5555, 99799, 6, 8),
        Reservation(2, 5, 55555, 999944, null , 8)
    )


    private val _employees : MutableLiveData<MutableList<Employee>> = MutableLiveData(listEmployees)
    val employees: LiveData<MutableList<Employee>> = _employees
    private val _reservations : MutableLiveData<MutableList<Reservation>> = MutableLiveData(listReservations)
    val reservations: LiveData<MutableList<Reservation>> = _reservations

    val employeesAdapter = EmployeesAdapterRecyclerView(listEmployees)
    val reservationAdapter = ReservationsAdapterRecyclerView(listReservations)

    fun updateData() {
        getEmployees()
    }

    private fun getEmployees() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                delay(3000L)
            }
            _employees.value?.addAll(listEmployees)
            employeesAdapter.addItems(_employees.value?.toList()?: emptyList())
        }
    }

    private fun getReservationsFromServer(context: Context) {
        viewModelScope.launch {
            val response = NetClient.instance.getReservationsByAdmin(0)
            if (response.isSuccessful) {
                response.body()?.let { reservationAdapter.addItems(it) }
            }
        }
    }

}