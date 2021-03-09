package com.example.bookingapp.admin.ui.reservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookingapp.R
import com.example.bookingapp.admin.ui.ManagerViewModel

class ReservationsFragment : Fragment() {

    private val viewModel: ManagerViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_reservations_admin, container, false)

        val reservationsRecycler: RecyclerView = root.findViewById(R.id.reservations_recycler_admin)
        with(reservationsRecycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModel.reservationAdapter
            isNestedScrollingEnabled = false
        }
        return root
    }
}