package com.example.bookingapp.user

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.bookingapp.*

class UserActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.user_refresh)
        swipeRefreshLayout.setOnRefreshListener {
            userViewModel.updateData(applicationContext)
            swipeRefreshLayout.isRefreshing = false
        }
        swipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    override fun onStart() {
        super.onStart()
        userViewModel.updateData(applicationContext)
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

                val intent = Intent(this@UserActivity, LoginActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}