@file:OptIn(ExperimentalUuidApi::class)

package com.asimorphic.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import org.koin.compose.viewmodel.koinViewModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun DialogSheetScopedViewModel(
    isVisible: Boolean,
    scopeId: String = rememberSaveable { Uuid.random().toString() },
    content: @Composable () -> Unit
) {
    val parentOwner = LocalViewModelStoreOwner.current
        ?: throw IllegalStateException("Parent owner not found")

    val registry = koinViewModel<ScopedStoreRegistryViewModel>(
        viewModelStoreOwner = parentOwner
    )

    var owner by remember { mutableStateOf<ViewModelStoreOwner?>(value = null) }

    // Dynamically create/clear view model owners
    LaunchedEffect(key1 = isVisible, key2 = scopeId) {
        if (isVisible && owner == null) {
            owner = object : ViewModelStoreOwner {
                override val viewModelStore: ViewModelStore
                    get() = registry.getOrCreate(id = scopeId)
            }
        } else if (!isVisible && owner != null) {
            registry.clear(id = scopeId)
            owner = null
        }
    }

    owner?.let { dialogOwner ->
        CompositionLocalProvider(
            value = LocalViewModelStoreOwner provides dialogOwner
        ) {
            content()
        }
    }
}