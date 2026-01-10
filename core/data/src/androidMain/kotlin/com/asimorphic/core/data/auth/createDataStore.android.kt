package com.asimorphic.core.data.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

fun createDataStore(
    context: Context
): DataStore<Preferences> {
    return createDataStore {
        context.filesDir.resolve(
            relative = DATA_STORE_FILE_NAME
        ).absolutePath
    }
}