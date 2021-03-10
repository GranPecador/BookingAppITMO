package com.example.bookingapp.user.ui.reservations

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookingapp.R
import com.example.bookingapp.user.NewReservationActivity
import com.example.bookingapp.user.UserViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ReservationsFragment : Fragment() {

    private val viewModel: UserViewModel  by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_reservations_user, container, false)
        activity?.title = getString(R.string.title_reservations)
        root.findViewById<FloatingActionButton>(R.id.new_reservation_floating_action_button).setOnClickListener {
            startActivity(Intent(context, NewReservationActivity::class.java))
        }
        root.findViewById<RecyclerView>(R.id.reservations_recycler_user).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModel.reservationAdapter
            isNestedScrollingEnabled = true
        }
        return root
    }
}