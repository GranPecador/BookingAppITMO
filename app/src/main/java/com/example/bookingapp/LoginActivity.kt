package com.example.bookingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.bookingapp.admin.ui.AdminActivity
import com.example.bookingapp.user.UserActivity
import com.example.bookingapp.waiter.WaiterActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginEdit = findViewById<TextInputEditText>(R.id.login_login_edit)

        findViewById<Button>(R.id.login_login_button).setOnClickListener {
            when(loginEdit.text.toString().trim()) {
                "admin" ->
                    startActivity(Intent(this, AdminActivity::class.java))
                "waiter" ->
                    startActivity(Intent(this, WaiterActivity::class.java))
                "user" -> startActivity(Intent(this, UserActivity::class.java))
            }
        }
    }
}