package com.example.bookingapp.user.ui.restaurants

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookingapp.R
import com.example.bookingapp.models.Restaurant

class RestaurantCardAdapterRecyclerView(val items: MutableList<Restaurant> = mutableListOf()) :
    RecyclerView.Adapter<RestaurantCardAdapterRecyclerView.RestaurantViewHolder>(){

    init{
        notifyDataSetChanged()
    }

    fun addItems(newItems: List<Restaurant>) {
        clearData()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun clearData() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_card_item, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RestaurantViewHolder,
        position: Int
    ) {
        holder.nameView.text = items[position].name
    }

    override fun getItemCount(): Int = items.size

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    data class RestaurantViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView = itemView.findViewById<TextView>(R.id.name_restaurant_text)
    }
}