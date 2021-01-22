package com.example.bookingapp.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bookingapp.CONST
import com.example.bookingapp.R
import com.example.bookingapp.models.NewReservation
import com.example.bookingapp.net.NetClient
import com.example.bookingapp.user.ui.restaurants.RestaurantsFragment
import kotlinx.coroutines.launch
import kotlin.random.Random

class NewReservationActivity : AppCompatActivity(), RestaurantsFragment.OnSelectRestaurantListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_reservation)

//        supportFragmentManager.commit {
//            setReorderingAllowed(true)
//            add<ExampleFragment>(R.id.fragment_container_view)
//        }

    }

    override fun onSelect(idRestaurant: Int) {
        lifecycleScope.launch {
            NetClient.instance.postReserve(
                CONST.ID_ROLE_USER,
                NewReservation(Random.nextInt(0, 100), CONST.ID_ROLE_USER, Random.nextInt(0, 100), idRestaurant)
            )
        }
    }

}