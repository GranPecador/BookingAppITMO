package com.example.bookingapp.admin.ui.reservations

import android.content.Context
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

    private var listenerReservations: OnCancelReservation? = null
    private val viewModel: ManagerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_reservations_admin, container, false)
        listenerReservations?.let { viewModel.reservationAdapter.setListener(it) }
        val reservationsRecycler: RecyclerView = root.findViewById(R.id.reservations_recycler_admin)
        with(reservationsRecycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModel.reservationAdapter
            isNestedScrollingEnabled = true
        }
        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCancelReservation) {
            listenerReservations = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listenerReservations = null
    }

    interface OnCancelReservation {
        fun onCancelReservation(userId: Int, reservationId: Int)
    }
}