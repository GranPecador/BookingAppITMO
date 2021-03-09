package com.example.bookingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bookingapp.admin.ui.ManagerActivity
import com.example.bookingapp.models.LoginPerson
import com.example.bookingapp.net.NetClient
import com.example.bookingapp.user.UserActivity
import com.example.bookingapp.waiter.WaiterActivity
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    lateinit var loginButton: Button
    lateinit var userInfoViewModel: UserInfoViewModel

    private lateinit var roleUser: ROLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userInfoViewModel = ViewModelProvider(
            this,
            UserInfoViewModelFactory(UserInfoPreferencesRepository.getInstance(this))
        ).get(UserInfoViewModel::class.java)

        val loginEdit = findViewById<TextInputEditText>(R.id.login_login_edit)
        val passwordEdit = findViewById<TextInputEditText>(R.id.password_login_edit)

        loginButton = findViewById<Button>(R.id.login_login_button)
        loginButton.setOnClickListener {
            loginButton.isEnabled = false
            val login = loginEdit.text.toString().trim()
            val password = passwordEdit.text.toString().trim()
            when (login) {
                "admin" ->
                    startActivity(Intent(this, ManagerActivity::class.java))
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

    private fun loginUser(login: String, password: String) {
        lifecycleScope.launch {
            try {
                val response =
                    NetClient.instance.postLogin(
                        LoginPerson(
                            login,
                            password
                        )
                    )
                if (response.isSuccessful) {
                    val authServ = response.headers()["Authorization"] ?: ""
                    Log.e("authServ", authServ)
                    userInfoViewModel.setAuth(authServ, applicationContext)
                    Log.e("authFlow", userInfoViewModel.userInfoFlow.first().auth)
                    requestInfoUser(authServ)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Проверьте правильность логина и пароля. ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: IOException) {
                Toast.makeText(
                    applicationContext,
                    "Повторите попытку. Возможно сервер уснул.",
                    Toast.LENGTH_LONG
                ).show()
            }
            loginButton.isEnabled = true
        }
    }

    private suspend fun requestInfoUser(auth: String) {
        try {
            val response =
                NetClient.instance.getUserInfo(auth)
            if (response.isSuccessful) {
                val body = response.body()
                val id = body?.id ?: -1
                roleUser = body?.role ?: ROLE.USER
                val roleId: Int = roleUser.ordinal
                val restaurantId = body?.restaurantId ?: -1

                userInfoViewModel.setIdsInfo(id, roleId, restaurantId, applicationContext)
                Log.e("authFlow", userInfoViewModel.userInfoFlow.first().auth)
                Log.e("role", body?.role.toString())
                Log.e("restaurantId", body?.restaurantId.toString())

                openActivity()
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

    private fun openActivity() {
        lifecycleScope.launch {
//            Log.e(
//                "role for open act", "${ROLE.values()[userInfoViewModel.userInfoFlow.first().roleId]} ${ROLE.values()[userInfoViewModel.userInfoFlow.first().roleId].ordinal} ${userInfoViewModel.userInfoFlow.first().roleId}"
//            )
        }
        var intent = Intent()
        when (roleUser) {
            ROLE.USER ->
                intent =
                    Intent(
                        applicationContext,
                        UserActivity::class.java
                    )
            ROLE.MANAGER ->
                intent =
                    Intent(
                        applicationContext,
                        ManagerActivity::class.java
                    )
            ROLE.WAITER -> intent =
                Intent(
                    applicationContext,
                    WaiterActivity::class.java
                )
            ROLE.ADMIN -> {

            }
        }
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

    }
}