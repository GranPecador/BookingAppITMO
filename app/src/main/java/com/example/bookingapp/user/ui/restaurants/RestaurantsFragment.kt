package com.example.bookingapp.user.ui.restaurants

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookingapp.R
import com.example.bookingapp.user.NewReservationViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.sql.Date

class RestaurantsFragment : Fragment() {

    private lateinit var startDropdown: AutoCompleteTextView
    private lateinit var endDropdown: AutoCompleteTextView

    private var listenerRestaurants: OnSelectRestaurantListener? = null
    private val viewModel:NewReservationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rstaurants, container, false)
        activity?.title = "Выберите ресторан:"
        viewModel.adapterRest.setListener(listenerRestaurants)
        startDropdown = view.findViewById(R.id.select_start_time_exposed_dropdown)
        startDropdown.setOnItemClickListener { parent, view, position, id ->
            viewModel.currentStart = times[id.toInt()].toInt()
        }
        endDropdown = view.findViewById(R.id.select_end_time_exposed_dropdown)
        endDropdown.setOnItemClickListener { parent, view, position, id ->
            viewModel.currentEnd = times[id.toInt()].toInt()
        }
        setTimes()
        val datePickerTextView = view.findViewById<TextView>(R.id.date_picker_restaurants_user_text_view)
        view.findViewById<RecyclerView>(R.id.restaurant_recycler).apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = viewModel.adapterRest
        }
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()
        picker.addOnPositiveButtonClickListener {
            datePickerTextView.text = Date(it).toString()
            viewModel.currentDate = Date(it)
            viewModel.dayOfMonth = Date(it).toString().subSequence(8,10).toString().toInt()
        }

        datePickerTextView.setOnClickListener {
            activity?.supportFragmentManager?.let { it1 -> picker.show(it1, picker.toString()) }
        }

        return view
    }

    val times = arrayOf("10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21")

    private fun setTimes() {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_menu_popup_item,
            times
        )
        startDropdown.setAdapter(adapter)
        endDropdown.setAdapter(adapter)
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