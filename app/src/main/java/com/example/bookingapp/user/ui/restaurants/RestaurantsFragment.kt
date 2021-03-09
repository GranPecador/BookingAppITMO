package com.example.bookingapp.user.ui.restaurants

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookingapp.R
import com.example.bookingapp.user.NewReservationViewModel

class RestaurantsFragment : Fragment() {


    private var listenerRestaurants: OnSelectRestaurantListener? = null
    private val viewModel:NewReservationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rstaurants, container, false)
        activity?.title = "Выберите ресторан:"
        viewModel.adapterRest.setListener(listenerRestaurants)
        view.findViewById<RecyclerView>(R.id.restaurant_recycler).apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = viewModel.adapterRest
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSelectRestaurantListener) {
            listenerRestaurants = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listenerRestaurants = null
    }


    interface OnSelectRestaurantListener {
        fun onSelectRestaurant(idRestaurant:Int)
        fun onSelectTable(idTable: Int)
    }
}