package com.example.bookingapp.waiter.ui.table

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.bookingapp.R
import com.example.bookingapp.waiter.WaiterViewModel

class TableFragment : Fragment() {

    private val viewModel: WaiterViewModel by activityViewModels()

    private lateinit var statusTableDropdown: AutoCompleteTextView
    private lateinit var tablesDropdown: AutoCompleteTextView

    private var currentSelectedStatus = -1
    private var currentSelectedTable = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_table, container, false)
        statusTableDropdown = root.findViewById(R.id.select_free_exposed_dropdown)
        statusTableDropdown.setOnItemClickListener { parent, view, position, id ->
            currentSelectedStatus = id.toInt()
        }
        setRoles()
        tablesDropdown = root.findViewById(R.id.select_table_exposed_dropdown)
        tablesDropdown.setOnItemClickListener { parent, view, position, id ->
            currentSelectedTable = id.toInt()
        }
        setTables()
        root.findViewById<ListView>(R.id.show_tables_status_table_text).apply {
            adapter = viewModel.tablesStatusAdapter
        }
        root.findViewById<Button>(R.id.change_status_table_table_button).setOnClickListener {
            Toast.makeText(
                requireContext(), "${viewModel.tables.value?.get(currentSelectedTable)} ${
                    STATUSES_OF_TABLE[currentSelectedStatus]
                } ", Toast.LENGTH_SHORT
            ).show()
            viewModel.putNewStatusOfTable(currentSelectedTable, currentSelectedStatus, requireContext())
        }
        return root
    }

    val STATUSES_OF_TABLE = arrayOf("Свободен", "Занят")

    private fun setRoles() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            STATUSES_OF_TABLE
        )
        statusTableDropdown.setAdapter(adapter)
    }

    private fun setTables() {
        tablesDropdown.setAdapter(viewModel.tablesDropdownAdapter)
    }
}