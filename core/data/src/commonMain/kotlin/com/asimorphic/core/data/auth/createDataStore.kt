package com.asimorphic.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

internal const val DATA_STORE_FILE_NAME = "pref.preferences_pb"

fun createDataStore(
    producePath: () -> String
): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath {
        producePath().toPath()
    }
}