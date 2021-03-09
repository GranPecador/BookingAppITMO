package com.example.bookingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bookingapp.models.RegisterPerson
import com.example.bookingapp.net.NetClient
import com.example.bookingapp.user.UserActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class RegistrationActivity : AppCompatActivity() {

    lateinit var userInfoViewModel: UserInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        userInfoViewModel = ViewModelProvider(
            this,
            UserInfoViewModelFactory(UserInfoPreferencesRepository.getInstance(this))
        ).get(UserInfoViewModel::class.java)

        val loginEdit = findViewById<TextInputEditText>(R.id.registration_login_edt)
        val passwordEdit = findViewById<TextInputEditText>(R.id.registration_password_edt)
        val phoneEdit = findViewById<TextInputEditText>(R.id.registration_telephone_edt)
        val nameEdit = findViewById<TextInputEditText>(R.id.registration_name_edt)
        val createUserBtn = findViewById<MaterialButton>(R.id.registration_create_btn)
        createUserBtn.setOnClickListener {

            val login = loginEdit.text.toString().trim()
            val password = passwordEdit.text.toString().trim()
            val phone = phoneEdit.text.toString().trim()
            val name = nameEdit.text.toString().trim()

            if (login.isEmpty()) {
                loginEdit.error = "Login required!"
                loginEdit.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                passwordEdit.error = "Password required!"
                passwordEdit.requestFocus()
                return@setOnClickListener
            }
            if (phone.isEmpty()) {
                phoneEdit.error = "Phone required!"
                phoneEdit.requestFocus()
                return@setOnClickListener
            }
            if (name.isEmpty()) {
                nameEdit.error = "Name required!"
                nameEdit.requestFocus()
                return@setOnClickListener
            }
            lifecycleScope.launch(Dispatchers.Main) {
                try {
                    val response = withContext(Dispatchers.IO) {
                        NetClient.instance.postRegister(
                            RegisterPerson(
                                login,
                                password,
                                name = name,
                                phone = phone
                            )
                        )
                    }
                    if (response.isSuccessful) {
                        val body = response.body()
                        val id = body?.id ?: -1
                        val role = body?.role ?: ROLE.USER
                        val restaurantId = -1
                        userInfoViewModel.setIdsInfo(
                            id,
                            role.ordinal,
                            restaurantId,
                            applicationContext
                        )
                        Log.e("role", body?.role.toString())
                        //Log.e("restaurantId",  body?.restaurantId.toString())

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
        }
    }

    private fun openActivity() {
        val intent = Intent(
            applicationContext,
            UserActivity::class.java
        )
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}