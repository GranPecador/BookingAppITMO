package com.example.bookingapp.admin.ui

import android.R
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingapp.ROLE
import com.example.bookingapp.UserInfoPreferencesRepository
import com.example.bookingapp.admin.ui.employees.EmployeesAdapterRecyclerView
import com.example.bookingapp.admin.ui.reservations.ReservationsAdapterRecyclerView
import com.example.bookingapp.models.Employee
import com.example.bookingapp.models.Reservation
import com.example.bookingapp.models.UserInfo
import com.example.bookingapp.net.NetClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ManagerViewModel : ViewModel() {

    private val listReservations = mutableListOf(
        Reservation(0, 5, 555, 9999, 5, 4),
        Reservation(1, 8, 5555, 99799, 6, 8),
        Reservation(2, 5, 55555, 999944, null, 8)
    )

    private val _employees: MutableLiveData<MutableList<Employee>> =
        MutableLiveData(mutableListOf())
    val employees: LiveData<MutableList<Employee>> = _employees
    private val _reservations: MutableLiveData<MutableList<Reservation>> =
        MutableLiveData(listReservations)
    val reservations: LiveData<MutableList<Reservation>> = _reservations
    private val _tables: MutableLiveData<MutableList<Int>> = MutableLiveData(mutableListOf())
    val tables: LiveData<MutableList<Int>> = _tables

    val employeesAdapterRecycler = EmployeesAdapterRecyclerView()
    val reservationAdapter = ReservationsAdapterRecyclerView(listReservations)
    lateinit var employeeDropdownAdapter: ArrayAdapter<String>
    lateinit var tablesDropdownAdapter: ArrayAdapter<String>

    private fun getNameUsers() = employees.value?.filter { it.role == ROLE.WAITER }?.map { it.name }
        ?: mutableListOf()

    private fun getTables() = tables.value?.map { "Стол №${it}" } ?: emptyList()

    fun createAdapters(context: Context) {
        createEmployeeAdapter(context)
        createTablesDropdownMenuAdapter(context)
    }

    private fun createEmployeeAdapter(context: Context) {
        val list = getNameUsers()
        employeeDropdownAdapter = ArrayAdapter(
            context,
            R.layout.simple_list_item_1,
            list
        )
    }

    private fun createTablesDropdownMenuAdapter(context: Context) {
        val tables = getTables()
        tablesDropdownAdapter = ArrayAdapter(
            context,
            R.layout.simple_list_item_1,
            tables
        )
    }

    fun updateData(context: Context) {
        getEmployees(context)
        getReservationsFromServer(context)
        getTablesByRestaurant(context)
    }

    private fun getEmployees(context: Context) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                NetClient.instance.getEmployeesByRestaurant(
                    UserInfoPreferencesRepository.getInstance(
                        context
                    ).userInfoPreferencesFlow.first().restaurantId
                )
            }
            if (response.isSuccessful) {
                val listEmployees = response.body() ?: emptyList()
                _employees.value?.addAll(listEmployees)
                employeesAdapterRecycler.addItems(listEmployees)
                employeeDropdownAdapter.clear()
                employeeDropdownAdapter.addAll(getNameUsers())
                employeeDropdownAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun getTablesByRestaurant(context: Context) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    NetClient.instance.getTablesByRestaurant(
                        UserInfoPreferencesRepository.getInstance(
                            context
                        ).userInfoPreferencesFlow.first().restaurantId,
                    )
                }
                if (response.isSuccessful) {
                    response.body()?.let {
                        _tables.value?.clear()
                        it.forEach { tableInfo ->
                            _tables.value?.add(tableInfo.id)
                        }
                        tablesDropdownAdapter.clear()
                        tablesDropdownAdapter.addAll(getTables())
                        tablesDropdownAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Не получилось загрузить столы, обновите страницу",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: IOException) {
                Toast.makeText(
                    context,
                    "Не получилось подключиться к серверу. Повторите попытку.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun getReservationsFromServer(context: Context) {
        viewModelScope.launch {
            val restaurantId =
                UserInfoPreferencesRepository.getInstance(context).userInfoPreferencesFlow.first().restaurantId
            try {
                val response = withContext(Dispatchers.IO) {
                    NetClient.instance.getReservationsByRestaurant(restaurantId)
                }
                if (response.isSuccessful) {
                    response.body()?.let {
                        reservationAdapter.addItems(it.reservations)
                    }
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

    fun addEmployee(name: String, login: String, password: String, role: ROLE, context: Context) {
        viewModelScope.launch {
            val restaurantId =
                UserInfoPreferencesRepository.getInstance(context).userInfoPreferencesFlow.first().restaurantId
            try {
                val response = withContext(Dispatchers.IO) {
                    NetClient.instance.postAddEmployee(
                        restaurantId,
                        UserInfo(
                            -1,
                            login,
                            password,
                            name,
                            "",
                            role,
                            restaurantId
                        )
                    )
                }
                if (response.isSuccessful) {
                    Toast.makeText(context, "Рабочий добавлен в систему", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(
                        context,
                        "Не получилось добавить пользователя.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: IOException) {
                Toast.makeText(
                    context,
                    "Не получилось подключиться к серверу. Повторите попытку.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun updateTableByEmployee(
        currentIdSelectedTableId: Int,
        currentIdSelectedEmployee: Int,
        context: Context
    ) {
        val employeeId = employees.value?.get(currentIdSelectedEmployee)?.id ?: -1
        val tableId = tables.value?.get(currentIdSelectedTableId) ?: -1
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    NetClient.instance.postUpdateAssociateTable(
                        UserInfoPreferencesRepository.getInstance(context).userInfoPreferencesFlow.first().restaurantId,
                        tableId, employeeId
                        //AssociateTableIdEmployeeId(tableId, employeeId)
                    )
                }
                if (response.isSuccessful) {
                    Toast.makeText(context, "Стол переназначен официанту", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(context, "Не получилось поменять стол", Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                Toast.makeText(
                    context,
                    "Не получилось подключиться к серверу. Повторите попытку.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun cancelReservation(userId: Int, reservationId: Int, context: Context) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    NetClient.instance.getCancelReservation(
                        userId, reservationId
                    )
                }
                if (response.isSuccessful) {
                    getReservationsFromServer(context)
                } else {
                    Toast.makeText(context, "Не получилось отменить резервацию", Toast.LENGTH_LONG)
                        .show()
                }
            } catch (e: IOException) {
                Toast.makeText(
                    context,
                    "Не получилось подключиться к серверу. Повторите попытку.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}