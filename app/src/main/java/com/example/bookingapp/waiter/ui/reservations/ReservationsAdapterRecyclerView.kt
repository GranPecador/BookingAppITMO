package com.example.bookingapp.waiter.ui.reservations

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookingapp.R
import com.example.bookingapp.models.Reservation
import java.text.SimpleDateFormat
import java.util.*

class ReservationsAdapterRecyclerView(val items: MutableList<Reservation> = mutableListOf()) :
    RecyclerView.Adapter<ReservationsAdapterRecyclerView.ReservationViewHolder>() {

    init {
        notifyDataSetChanged()
    }

    fun addItems(newItems: List<Reservation>) {
        clearData()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun clearData() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.reservation_item, parent, false)
        return ReservationViewHolder(view)
    }

    private val simpleDateFormat = SimpleDateFormat("H:mm dd.MM.yy", Locale.getDefault())
    private fun getDateString(time: Int): String = simpleDateFormat.format(time * 1000L)

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val item = items[position]
        holder.idView.text = "Номер брони: ${item.id}"
        holder.tableView.text = "Стол ${item.numberName}"
        try {
            holder.startTimeView.text = "c ${getDateString(item.dateStartReservation.toInt())}"
            holder.endTimeView.text = " до ${getDateString(item.dateEndReservation.toInt())}"
        } catch (e: Exception) {
            Log.e("Error date", e.toString())
        }
        if (items[position].order == null) {
            holder.orderView.visibility = View.GONE
        } else {
            holder.orderView.text = "Заказ:\n${item.order}"
        }
    }

    override fun getItemCount(): Int = items.size

    class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idView: TextView = itemView.findViewById(R.id.id_reservation_text_view_item)
        val tableView: TextView = itemView.findViewById(R.id.table_number_text_view_item)
        val startTimeView: TextView =
            itemView.findViewById(R.id.start_reservatjion_time_text_view_item)
        val endTimeView: TextView = itemView.findViewById(R.id.end_reservation_time_text_view_item)
        val orderView: TextView = itemView.findViewById(R.id.order_reservation_text_view_item)
    }

}