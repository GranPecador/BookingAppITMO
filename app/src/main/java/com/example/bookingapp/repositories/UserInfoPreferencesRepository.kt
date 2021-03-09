package com.example.bookingapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

data class UserInfoPreferences(
    var auth: String = "",
    var id: Int = -1,
    var roleId: Int = 0,
    var restaurantId: Int = -1)

class UserInfoPreferencesRepository private constructor(context: Context) {

    private object PreferencesKeys {
        val AUTH = stringPreferencesKey("auth")
        val ID = intPreferencesKey("id")
        val ROLE_USER = intPreferencesKey("role_user")
        val RESTAURANT_ID = intPreferencesKey("restaurant_id")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "info_user")

    val userInfoPreferencesFlow: Flow<UserInfoPreferences> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val auth = preferences[PreferencesKeys.AUTH] ?: ""
            val id = preferences[PreferencesKeys.ID] ?: -1
            val restaurantId = preferences[PreferencesKeys.RESTAURANT_ID] ?: -1
            val role: Int = preferences[PreferencesKeys.ROLE_USER] ?: 0
            UserInfoPreferences(auth, id, role, restaurantId)
        }

    // Write data to DataStore
    suspend fun writeUserInfo(newId: Int, newRoleId: Int, newRestaurantId:Int, context: Context) {
        context.dataStore.edit { flags ->
            flags[PreferencesKeys.ID] = newId
            flags[PreferencesKeys.ROLE_USER] = newRoleId
            flags[PreferencesKeys.RESTAURANT_ID] = newRestaurantId
        }
    }

    suspend fun writeAuth(newAuth: String, context: Context) {
        context.dataStore.edit { flags ->
            flags[PreferencesKeys.AUTH] = newAuth
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserInfoPreferencesRepository? = null

        fun getInstance(context: Context): UserInfoPreferencesRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = UserInfoPreferencesRepository(context)
                INSTANCE = instance
                instance
            }
        }
    }
}