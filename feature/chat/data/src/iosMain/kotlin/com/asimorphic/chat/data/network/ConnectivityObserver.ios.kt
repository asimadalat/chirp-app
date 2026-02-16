package com.asimorphic.chat.data.network

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfiable
import platform.Network.nw_path_status_satisfied
import platform.darwin.dispatch_queue_create

actual class ConnectivityObserver {
    actual val isConnected: Flow<Boolean> = callbackFlow {
        val pathMonitor = nw_path_monitor_create()

        val dispatchQueue = dispatch_queue_create(
            label =NW_PATH_MONITOR_LABEL,
            attr = null
        )

        nw_path_monitor_set_update_handler(pathMonitor) { path ->
            path?.let {
                val status = nw_path_get_status(path = path)

                val isConnected = when (status) {
                    nw_path_status_satisfied -> true
                    nw_path_status_satisfiable -> true
                    else -> false
                }

                trySend(element = isConnected)
            }
        }

        nw_path_monitor_set_queue(
            monitor = pathMonitor,
            queue = dispatchQueue
        )
        nw_path_monitor_start(monitor = pathMonitor)

        awaitClose {
            nw_path_monitor_cancel(monitor = pathMonitor)
        }
    }

    companion object {
        private const val NW_PATH_MONITOR_LABEL = "com.asimorphic.chat.data.network.ConnectivityObserver"
    }
}