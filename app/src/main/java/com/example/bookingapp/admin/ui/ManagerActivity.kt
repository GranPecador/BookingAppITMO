package com.example.bookingapp.admin.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.bookingapp.*
import com.example.bookingapp.admin.ui.reservations.ReservationsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class ManagerActivity : AppCompatActivity(), ReservationsFragment.OnCancelReservation {

    private val managerViewModel: ManagerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        managerViewModel.createAdapters(applicationContext)
        managerViewModel.updateData(applicationContext)

        val navView: BottomNavigationView = findViewById(R.id.admin_nav_view)

        val navController = findNavController(R.id.admin_nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.admin_navigation_reservations, R.id.navigation_employees, R.id.navigation_add_employees
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.admin_refresh)
        swipeRefreshLayout.setOnRefreshListener {
            managerViewModel.updateData(applicationContext)
            swipeRefreshLayout.isRefreshing = false
        }
        swipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.exit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.exit -> {
                val userInfoViewModel = ViewModelProvider(
                    this,
                    UserInfoViewModelFactory(UserInfoPreferencesRepository.getInstance(this))
                ).get(UserInfoViewModel::class.java)
                userInfoViewModel.setIdsInfo(-1, -1, -1, applicationContext)
                userInfoViewModel.setAuth("", applicationContext)

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCancelReservation(userId: Int, reservationId: Int) {
        managerViewModel.cancelReservation(userId, reservationId, applicationContext)
    }
}