package com.example.mymovie.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData


class NetworkStateLiveData(private val context: Context) : LiveData<Boolean>() {

    private val connectivityManager: ConnectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateNetworkState()
        }
    }

    override fun onActive() {
        super.onActive()
        context.registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        updateNetworkState()
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(networkReceiver)
    }

    private fun updateNetworkState() {
        val networkInfo = connectivityManager.activeNetworkInfo
        value = networkInfo != null && networkInfo.isConnected
    }
}
