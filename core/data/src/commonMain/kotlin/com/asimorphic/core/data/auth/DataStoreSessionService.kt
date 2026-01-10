package com.asimorphic.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.asimorphic.core.data.mapper.toDto
import com.asimorphic.core.domain.auth.SessionService
import com.asimorphic.core.domain.model.AuthCredential
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class DataStoreSessionService(
    private val dataStore: DataStore<Preferences>
): SessionService {

    private val authCredentialKey = stringPreferencesKey(
        name = "KEY_AUTH_CREDENTIAL"
    )

    private val json = Json { ignoreUnknownKeys = true }

    override fun observeAuthCredential(): Flow<AuthCredential?> {
        return dataStore.data.map { preferences ->
            val serialisedJson = preferences[authCredentialKey]
            serialisedJson?.let {
                json.decodeFromString(string = it)
            }
        }
    }

    override suspend fun set(credential: AuthCredential?) {
        if (credential == null) {
            dataStore.edit {
                it.remove(key = authCredentialKey)
            }
            return
        }

        val serialised = json.encodeToString(
            value = credential.toDto()
        )

        dataStore.edit { preferences ->
            preferences[authCredentialKey] = serialised
        }
    }
}