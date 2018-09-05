package com.gutoomota.cuboschallenge.util

import android.content.Context
import android.net.ConnectivityManager

class NetworkObserver private constructor() {

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    companion object {

        private var instance: NetworkObserver? = null

        @Synchronized
        fun getInstance(): NetworkObserver {
            if (instance == null) {
                instance = NetworkObserver()
            }
            return instance as NetworkObserver
        }
    }
}
