package com.example.bookingapp.admin.ui.employees

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.bookingapp.R
import com.example.bookingapp.admin.ui.AdminViewModel
import com.example.bookingapp.models.Employee


class AddEmployeeFragment : Fragment() {

    private val viewModel: AdminViewModel by activityViewModels()

    private lateinit var rolesDropdown: AutoCompleteTextView
    private lateinit var employeesDropdown: AutoCompleteTextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_employee_admin, container, false)
        rolesDropdown = view.findViewById(R.id.select_role_exposed_dropdown)
        employeesDropdown = view.findViewById(R.id.select_employee_exposed_dropdown)
        setRoles()
        setEmployees()

        val addEmployeeButton: Button = view.findViewById(R.id.add_add_employee_button)
        addEmployeeButton.setOnClickListener {

        }

        val updateTableButton: Button = view.findViewById(R.id.update_change_table_button)
        updateTableButton.setOnClickListener {

        }

        return view
    }

    private fun setRoles() {
        val ROLES = arrayOf("Администратор", "Официант")
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_menu_popup_item,
            ROLES
        )
        rolesDropdown.setAdapter(adapter)
    }

    private fun setEmployees() {
        val list : List<String> = viewModel.employees.value?.map {it.name} ?: emptyList()
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            list)
        employeesDropdown.setAdapter(adapter)
    }
}