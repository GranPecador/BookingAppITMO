package com.example.bookingapp.user.ui.reservations

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookingapp.R
import com.example.bookingapp.user.NewReservationActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ReservationsFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_reservations_user, container, false)
        root.findViewById<FloatingActionButton>(R.id.new_reservation_floating_action_button).setOnClickListener {
            startActivity(Intent(context, NewReservationActivity::class.java))
        }
        root.findViewById<RecyclerView>(R.id.reservations_recycler_user).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModel.reservationAdapter
            isNestedScrollingEnabled = false
        }
        return root
    }
}