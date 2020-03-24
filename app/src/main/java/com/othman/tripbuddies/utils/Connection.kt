package com.othman.tripbuddies.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import java.util.*


object Connection {


    fun isWifiAvailable(context: Context): Boolean? {
        val wifi =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return Objects.requireNonNull(wifi).isWifiEnabled
    }

    fun isInternetAvailable(context: Context): Boolean? {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo = Objects.requireNonNull(cm).activeNetworkInfo
        return activeNetwork.isConnected
    }
}