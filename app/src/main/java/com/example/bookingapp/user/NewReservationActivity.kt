package com.example.bookingapp.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.bookingapp.R
import com.example.bookingapp.user.ui.restaurants.RestaurantsFragment

class NewReservationActivity : AppCompatActivity(), RestaurantsFragment.OnSelectRestaurant {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_reservation)

//        supportFragmentManager.commit {
//            setReorderingAllowed(true)
//            add<ExampleFragment>(R.id.fragment_container_view)
//        }

    }

    override fun onSelect(idRestaurant: Int) {
        TODO("Not yet implemented")
    }

}