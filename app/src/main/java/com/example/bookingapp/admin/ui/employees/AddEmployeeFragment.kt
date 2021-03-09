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
import com.example.bookingapp.ROLE
import com.example.bookingapp.admin.ui.ManagerViewModel
import com.google.android.material.textfield.TextInputEditText


class AddEmployeeFragment : Fragment() {

    private val viewModel: ManagerViewModel by activityViewModels()

    private lateinit var rolesDropdown: AutoCompleteTextView
    private lateinit var employeesDropdown: AutoCompleteTextView
    private lateinit var tableIdsDropdown: AutoCompleteTextView

    private lateinit var nameEdit: TextInputEditText
    private lateinit var loginEdit: TextInputEditText
    private lateinit var passwordEdit: TextInputEditText

    private var currentIdSelectedRole = -1
    private var currentIdSelectedEmployee = -1
    private var currentIdSelectedTableId = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_employee_admin, container, false)
        rolesDropdown = view.findViewById(R.id.select_role_exposed_dropdown)
        rolesDropdown.setOnItemClickListener { parent, view, position, id ->
            currentIdSelectedRole = id.toInt()
        }
        employeesDropdown = view.findViewById(R.id.select_employee_exposed_dropdown)
        employeesDropdown.setOnItemClickListener { parent, view, position, id ->
            currentIdSelectedEmployee = id.toInt()
        }
        tableIdsDropdown = view.findViewById(R.id.table_number_change_table_exposed_dropdown)
        tableIdsDropdown.setOnItemClickListener { parent, view, position, id ->
            currentIdSelectedTableId = id.toInt()
        }
        setRoles()
        setDropdowns()

        nameEdit = view.findViewById(R.id.fullname_add_employee_edit)
        loginEdit = view.findViewById(R.id.login_add_employee_edit)
        passwordEdit = view.findViewById(R.id.password_add_employee_edit)

        val addEmployeeButton: Button = view.findViewById(R.id.add_add_employee_button)
        addEmployeeButton.setOnClickListener {
            val name = nameEdit.text.toString()
            val login = loginEdit.text.toString()
            val password = passwordEdit.text.toString()
            val role = if (currentIdSelectedRole==0) ROLE.MANAGER else ROLE.WAITER
            viewModel.addEmployee(name, login, password, role, requireContext())
        }

        val updateTableButton: Button = view.findViewById(R.id.update_change_table_button)
        updateTableButton.setOnClickListener {
            viewModel.updateTableByEmployee(currentIdSelectedTableId, currentIdSelectedEmployee, requireContext())
        }

        return view
    }

    private fun setRoles() {
        val ROLES = arrayOf("Менеджер", "Официант")
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_menu_popup_item,
            ROLES
        )
        rolesDropdown.setAdapter(adapter)
    }

    private fun setDropdowns() {
        employeesDropdown.setAdapter(viewModel.employeeDropdownAdapter)
        tableIdsDropdown.setAdapter(viewModel.tablesDropdownAdapter)
    }
}