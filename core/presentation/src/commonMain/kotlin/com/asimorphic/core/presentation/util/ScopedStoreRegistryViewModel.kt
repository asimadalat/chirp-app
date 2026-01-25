package com.asimorphic.core.presentation.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore

class ScopedStoreRegistryViewModel: ViewModel() {
    private val stores = mutableMapOf<String, ViewModelStore>()

    fun getOrCreate(id: String): ViewModelStore =
        stores.getOrPut(key = id) { ViewModelStore()  }

    fun clear(id: String) {
        stores.remove(key = id)?.clear()
    }

    override fun onCleared() {
        super.onCleared()
        stores.values.forEach { it.clear() }
        stores.clear()
    }
}