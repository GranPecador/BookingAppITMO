package com.example.bookingapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserInfoViewModel(private val userInfoPreferencesRepository: UserInfoPreferencesRepository) :
    ViewModel() {

    val userInfoFlow = userInfoPreferencesRepository.userInfoPreferencesFlow

    fun setAuth(auth: String, context: Context) {
        viewModelScope.launch {
            userInfoPreferencesRepository.writeAuth(auth, context = context)
        }
    }

    fun setIdsInfo(id: Int, roleId: Int, restaurantId:Int, context: Context) {
        viewModelScope.launch {
            userInfoPreferencesRepository.writeUserInfo(id, roleId, restaurantId, context)
        }
    }
}

class UserInfoViewModelFactory(
    private val userInfoPreferencesRepository: UserInfoPreferencesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserInfoViewModel(userInfoPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}