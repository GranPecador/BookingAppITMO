package com.example.bookingapp.user

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bookingapp.R
import com.example.bookingapp.user.ui.restaurants.RestaurantsFragment

class NewReservationActivity : AppCompatActivity(), RestaurantsFragment.OnSelectRestaurantListener {

    private val viewModel:NewReservationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_reservation)
    }

    override fun onSelectRestaurant(idRestaurant: Int) {
        viewModel.setSelectedRestaurantId(idRestaurant)
        viewModel.getTableForRecycler(idRestaurant, applicationContext)
    }

    override fun onSelectTable(idTable: Int) {
        viewModel.setSelectedTableId(idTable)
        viewModel.createReservation(applicationContext)
    }
}