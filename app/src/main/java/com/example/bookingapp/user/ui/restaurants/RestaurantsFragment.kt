package com.example.bookingapp.user.ui.restaurants

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookingapp.R
import com.example.bookingapp.models.Restaurant

class RestaurantsFragment : Fragment() {

    private val restaurants = mutableListOf(Restaurant(0, "Restaurant0"), Restaurant(1, "Restaurant1"))

    private var listenerRestaurants: OnSelectRestaurantListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rstaurants, container, false)
        activity?.title = "Выберите ресторан:"
        view.findViewById<RecyclerView>(R.id.restaurant_recycler).apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = RestaurantCardAdapterRecyclerView(restaurants, listenerRestaurants)
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
        fun onSelect(idRestaurant:Int)
    }
}