package ar.com.logiciel.cptmobile.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.navigationDataStore: DataStore<Preferences> by preferencesDataStore(name = "navigation_preferences")

@Singleton
class NavigationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.navigationDataStore

    companion object {
        val LAST_SCREEN_KEY = stringPreferencesKey("last_screen")
    }

    val lastScreen: Flow<String?> = dataStore.data.map { preferences ->
        preferences[LAST_SCREEN_KEY]
    }

    suspend fun saveLastScreen(screen: String) {
        dataStore.edit { preferences ->
            preferences[LAST_SCREEN_KEY] = screen
        }
    }
}
