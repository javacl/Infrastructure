package ir.javad.infrastructure.core.utils

import android.content.Context
import android.net.ConnectivityManager

// Application Check Network
class NetworkHandler(private val context: Context) {

    @Suppress("DEPRECATION")
    fun hasNetworkConnection(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
