package pam.tugas5.romadhon

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

actual class NetworkMonitor {
    actual fun isConnected(): Boolean {
        return true
    }

    actual fun observeConnectivity(): Flow<Boolean> {
        return flowOf(true)
    }
}