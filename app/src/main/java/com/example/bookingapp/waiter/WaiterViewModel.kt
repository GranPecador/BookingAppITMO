package com.example.bookingapp.waiter

import android.R
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingapp.UserInfoPreferencesRepository
import com.example.bookingapp.models.Reservation
import com.example.bookingapp.net.NetClient
import com.example.bookingapp.waiter.ui.reservations.ReservationsAdapterRecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class WaiterViewModel() : ViewModel() {

    private val listReservations = mutableListOf(
        Reservation(0, 5, 555, 9999, 5, 4),
        Reservation(1, 8, 5555, 99799, 6, 8),
        Reservation(2, 5, 55555, 999944, null, 8)
    )
    private val listTable = mutableListOf(5, 10)

    private val _reservations: MutableLiveData<MutableList<Reservation>> =
        MutableLiveData(listReservations)
    val reservations: LiveData<MutableList<Reservation>> = _reservations
    private val _tables: MutableLiveData<MutableList<Int>> = MutableLiveData(listTable)
    val tables: LiveData<MutableList<Int>> = _tables

    private val tablesStatus: MutableList<Map<String, String>> =
        mutableListOf(hashMapOf(Pair("tableStatus", "Стол № : занят")))

    val reservationAdapter = ReservationsAdapterRecyclerView(listReservations)
    lateinit var tablesStatusAdapter: SimpleAdapter
    lateinit var tablesDropdownAdapter: ArrayAdapter<String>

    fun updateData(context: Context) {
        getTablesByEmployee(context)
        getReservationsFromServer(context)
    }

    private fun getTables() = tables.value?.map { "Стол №${it}" } ?: emptyList()


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

    fun createAdapters(context: Context) {
        createTablesDropdownMenuAdapter(context)
        createTablesStatusAdapter(context)
    }

    private fun createTablesDropdownMenuAdapter(context: Context) {
        val tables = getTables()
        tablesDropdownAdapter = ArrayAdapter(
            context,
            R.layout.simple_list_item_1,
            tables
        )
    }

    private fun createTablesStatusAdapter(context: Context) {
        tablesStatusAdapter = SimpleAdapter(
            context,
            tablesStatus,
            R.layout.simple_list_item_2,
            arrayOf("tableStatus"),
            intArrayOf(R.id.text1)
        )
    }

    private fun getTablesByEmployee(context: Context) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                NetClient.instance.getTablesByEmployee(
                    UserInfoPreferencesRepository.getInstance(
                        context
                    ).userInfoPreferencesFlow.first().restaurantId,
                    UserInfoPreferencesRepository.getInstance(context).userInfoPreferencesFlow.first().id
                )
            }
            if (response.isSuccessful) {
                response.body()?.let {
                    tablesStatus.clear()
                    _tables.value?.clear()
                    it.forEach { tableInfo ->
                        val tableId = tableInfo.id
                        val status = if (tableInfo.isFree) "свободен" else "занят"
                        tablesStatus.add(
                            hashMapOf(
                                Pair(
                                    "tableStatus",
                                    "Стол №${tableId} : $status"
                                )
                            )
                        )
                        _tables.value?.add(tableId)
                    }
                    tablesStatusAdapter.notifyDataSetChanged()
                    tablesDropdownAdapter.clear()
                    tablesDropdownAdapter.addAll(getTables())
                    tablesDropdownAdapter.notifyDataSetChanged()
                }
            } else {
                Toast.makeText(
                    context,
                    "Не получилось загрузить статусы столов, обновите страницу",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun putNewStatusOfTable(
        currentSelectedTable: Int,
        currentSelectedStatus: Int,
        context: Context
    ) {
        val tableId = tables.value?.get(currentSelectedTable) ?: -1
        val isFree = currentSelectedStatus == 0
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                NetClient.instance.putNewStatusOfTable(
                    UserInfoPreferencesRepository.getInstance(context).userInfoPreferencesFlow.first().restaurantId,
                    isFree,
                    tableId
                )
            }
            if (response.isSuccessful) {
                getTablesByEmployee(context)
            } else {
                Toast.makeText(
                    context,
                    "Не получилось загрузить статусы столов, обновите страницу",
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