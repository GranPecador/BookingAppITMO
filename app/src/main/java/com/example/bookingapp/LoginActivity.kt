package com.example.bookingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.bookingapp.admin.ui.AdminActivity
import com.example.bookingapp.models.LoginPerson
import com.example.bookingapp.net.NetClient
import com.example.bookingapp.user.UserActivity
import com.example.bookingapp.waiter.WaiterActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginEdit = findViewById<TextInputEditText>(R.id.login_login_edit)
        val passwordEdit = findViewById<TextInputEditText>(R.id.password_login_edit)

        findViewById<Button>(R.id.login_login_button).setOnClickListener {
            val login = loginEdit.text.toString().trim()
            val password = passwordEdit.text.toString().trim()
            when(login) {
                "admin" ->
                    startActivity(Intent(this, AdminActivity::class.java))
                "waiter" ->
                    startActivity(Intent(this, WaiterActivity::class.java))
                "user" -> startActivity(Intent(this, UserActivity::class.java))
                else -> loginUser(login, password)

            }
        }
        findViewById<Button>(R.id.registration_login_button).setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }

    private fun loginById(idUser:Int){

    }

    private fun loginUser(login:String, password:String) {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    NetClient.instance.postLogin(
                        LoginPerson(
                            login,
                            password
                        )
                    )
                }
                //Log.e("response", response.headers().toString())
                if (response.isSuccessful) {
                    val intent =
                        Intent(
                            this@LoginActivity,
                            UserActivity::class.java
                        )
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: IOException) {
                Toast.makeText(
                    applicationContext,
                    "Повторите попытку",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}