package com.asimorphic.core.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.asimorphic.core.domain.preferences.ThemePreference
import com.asimorphic.core.domain.preferences.ThemePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreThemePreferences(
    private val dataStore: DataStore<Preferences>
): ThemePreferences {

    private val themePreferenceKey = stringPreferencesKey(name = "theme_preference")

    override fun observeThemePreference(): Flow<ThemePreference> =
        dataStore.data.map { preferences ->
            val currentPreference = preferences[themePreferenceKey] ?: ThemePreference.SYSTEM.name
            try {
                ThemePreference.valueOf(currentPreference)
            } catch (_: Exception) {
                ThemePreference.SYSTEM
            }
        }


    override suspend fun updateThemePreference(theme: ThemePreference) {
        dataStore.edit { preferences ->
            preferences[themePreferenceKey] = theme.name
        }
    }
}