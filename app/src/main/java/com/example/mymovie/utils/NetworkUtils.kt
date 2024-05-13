package com.example.mymovie.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData


class ConnectionLiveData(val context: Context) : LiveData<Boolean>() {

    companion object {
        private lateinit var connectionLiveData: ConnectionLiveData

        @MainThread
        fun get(context: Context): ConnectionLiveData {
            connectionLiveData =
                if (::connectionLiveData.isInitialized) connectionLiveData else ConnectionLiveData(
                    context
                )
            return connectionLiveData
        }
    }

    private var connectivityManager: ConnectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager


    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    override fun onActive() {
        super.onActive()
        updateConnection()
        connectivityManager.registerDefaultNetworkCallback(
            getConnectivityManagerCallback()
        )
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
    }

    private fun lollipopNetworkAvailableRequest() {
        val builder = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_VPN)

        connectivityManager.registerNetworkCallback(
            builder.build(),
            getConnectivityManagerCallback()
        )
    }

    private fun getConnectivityManagerCallback(): ConnectivityManager.NetworkCallback {

        connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {

                val activeNetwork = connectivityManager.getNetworkCapabilities(network)
                //Toast.makeText(context,"You are onAvailable",Toast.LENGTH_SHORT).show()
                if (activeNetwork != null) {

                    when {
                        // Indicates this network uses a Wi-Fi transport,
                        // or WiFi has network connectivity
                        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            postValue(true)
                        }

                        // Indicates this network uses a Cellular transport. or
                        // Cellular has network connectivity
                        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            postValue(true)
                        }

                        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                            postValue(false)

                        }

                        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            postValue(true)

                        }
                        else -> {
                            postValue(false)
                        }
                    }

                }

            }

            override fun onLost(network: Network) {
                postValue(false)
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                //Toast.makeText(context,"You are onCapabilitiesChanged",Toast.LENGTH_SHORT).show()
                val isValidated =
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                val isInternet =
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

                val isVPN =
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)

                val isWifi =
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                val isCellular =
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)


                //ConnectedOnline
                //VPNConnected (Online) => Wifi||Cellular true , VPN true
                //VPNConnected(Offline) => Wifi||Cellular false  , VPN true

                if (isVPN) {

                    val checkOnlineVpnConnected =
                        (isValidated && isInternet) && (isWifi || isCellular)
                    //Toast.makeText(context,"You are isVPN True {$checkOnlineVpnConnected}",Toast.LENGTH_SHORT).show()
                    postValue(checkOnlineVpnConnected)

                } else {

                    val checkOnlineVpnConnected = isWifi || isCellular
                    //Toast.makeText(context,"You are isVPN is False {$checkOnlineVpnConnected}",Toast.LENGTH_SHORT).show()
                    postValue(checkOnlineVpnConnected)

                }


            }
        }
        return connectivityManagerCallback
    }


    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateConnection()
        }
    }

    private fun updateConnection() {

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network)

            if (activeNetwork != null) {

                when {

                    // Indicates this network uses a Wi-Fi transport,
                    // or WiFi has network connectivity
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        postValue(true)
                    }
                    // Indicates this network uses a Cellular transport. or
                    // Cellular has network connectivity
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        postValue(true)
                    }

                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                        postValue(false)
                    }

                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        postValue(true)
                    }

                    // else return false
                    else -> {
                        postValue(false)
                    }
                }
            }

        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo
            @Suppress("DEPRECATION")
            postValue(networkInfo?.isConnected)

        }
    }
    fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }
}
val Context.isConnected: Boolean
    get() = ConnectionLiveData.get(applicationContext).isInternetAvailable(applicationContext)
