package com.example.bookingapp.admin.ui.employees

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookingapp.R
import com.example.bookingapp.admin.ui.ManagerViewModel

class EmployeesFragment : Fragment() {

    private val viewModel: ManagerViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_employees_admin, container, false)
        root.findViewById<RecyclerView>(R.id.employees_recycler).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModel.employeesAdapterRecycler
        }

        return root
    }
}