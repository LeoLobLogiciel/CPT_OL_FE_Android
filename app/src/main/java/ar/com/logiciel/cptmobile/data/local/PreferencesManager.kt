package ar.com.logiciel.cptmobile.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import ar.com.logiciel.cptmobile.core.constants.ApiConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val KEY_USER_TOKEN = stringPreferencesKey(ApiConstants.KEY_USER_TOKEN)
        val KEY_USER_ID = intPreferencesKey(ApiConstants.KEY_USER_ID)
        val KEY_USER_EMAIL = stringPreferencesKey(ApiConstants.KEY_USER_EMAIL)
        val KEY_USER_NAME = stringPreferencesKey(ApiConstants.KEY_USER_NAME)
        val KEY_IS_LOGGED_IN = booleanPreferencesKey(ApiConstants.KEY_IS_LOGGED_IN)
    }

    val userToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[KEY_USER_TOKEN]
    }

    val userId: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[KEY_USER_ID]
    }

    val userEmail: Flow<String?> = dataStore.data.map { preferences ->
        preferences[KEY_USER_EMAIL]
    }

    val userName: Flow<String?> = dataStore.data.map { preferences ->
        preferences[KEY_USER_NAME]
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[KEY_IS_LOGGED_IN] ?: false
    }

    suspend fun saveUserToken(token: String) {
        dataStore.edit { preferences ->
            preferences[KEY_USER_TOKEN] = token
        }
    }

    suspend fun saveUserId(userId: Int) {
        dataStore.edit { preferences ->
            preferences[KEY_USER_ID] = userId
        }
    }

    suspend fun saveUserEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[KEY_USER_EMAIL] = email
        }
    }

    suspend fun saveUserName(name: String) {
        dataStore.edit { preferences ->
            preferences[KEY_USER_NAME] = name
        }
    }

    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_IS_LOGGED_IN] = isLoggedIn
        }
    }

    suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
