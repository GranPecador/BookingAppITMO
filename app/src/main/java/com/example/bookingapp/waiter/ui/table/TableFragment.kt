package com.example.bookingapp.waiter.ui.table

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bookingapp.R
import com.example.bookingapp.waiter.WaiterViewModel

class TableFragment : Fragment() {

  private val viewModel: WaiterViewModel by activityViewModels()

  private lateinit var statusTableDropdown: AutoCompleteTextView
  private lateinit var tablesDropdown: AutoCompleteTextView

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val root = inflater.inflate(R.layout.fragment_table, container, false)
    statusTableDropdown = root.findViewById(R.id.select_free_exposed_dropdown)
    setRoles()
    tablesDropdown = root.findViewById(R.id.select_table_exposed_dropdown)
    setTables()

    return root
  }

  private fun setRoles() {
    val ROLES = arrayOf("Свободен", "Занят")
    val adapter = ArrayAdapter(
      requireContext(),
      android.R.layout.simple_list_item_1,
      ROLES
    )
    statusTableDropdown.setAdapter(adapter)
  }

  private fun setTables() {
    val tables = viewModel.getTables()
    val adapter = ArrayAdapter(
      requireContext(),
      android.R.layout.simple_list_item_1,
      tables
    )
    statusTableDropdown.setAdapter(adapter)
  }
}