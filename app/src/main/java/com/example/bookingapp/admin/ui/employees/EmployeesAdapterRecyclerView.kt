package com.example.bookingapp.admin.ui.employees

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookingapp.models.Employee

class EmployeesAdapterRecyclerView (val items: MutableList<Employee> = mutableListOf()) :
    RecyclerView.Adapter<EmployeesAdapterRecyclerView.EmployeeViewHolder>() {

    init {
        notifyDataSetChanged()
    }

    fun addItems(newItems: List<Employee>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun clearData() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return EmployeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val item = items[position]
        holder.nameView.text = item.name
        ///holder.tableView.text = "Стол №${item.reservationInfo.table.id}"
    }

    override fun getItemCount(): Int = items.size

    class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.findViewById(android.R.id.text1)
        //val tableView: TextView = itemView.findViewById(R.id.table_employee_text_view_item)
    }
}