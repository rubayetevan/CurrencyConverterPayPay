package com.codesignal.paypay.currencyconverter.views.activities

import android.content.Context
import android.net.*
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.codesignal.paypay.currencyconverter.databinding.ActivityMainBinding
import com.codesignal.paypay.currencyconverter.viewModels.MainViewModel
import com.codesignal.paypay.currencyconverter.views.adapters.CurrencyAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val currencyAdapter = CurrencyAdapter()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            viewModel.hasInternet(true)
        }

        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities,
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val hasCellular =
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            val hasWifi = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)

            viewModel.hasInternet(hasCellular || hasWifi)


        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            viewModel.hasInternet(false)
            showNoInternetToast()

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.mainViewModel = viewModel
        val view = binding.root
        setContentView(view)
        setupErrorMessageViewer()
        setupNetworkMonitor()
        setupSpinner()
        setupRV()
    }


    private fun setupNetworkMonitor() {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        viewModel.hasInternet(isConnected)
        if (!isConnected)
            showNoInternetToast()

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val connectivityManager =
            getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    private fun setupSpinner() {
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                if (viewModel.fromCurrencyPosition != position) {
                    viewModel.fromCurrencyPosition = position
                    viewModel.getCurrencyConvertedValue()
                }

                try {
                    val a: TextView = parent?.getChildAt(0) as TextView
                    a.textSize = 18f
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun setupRV() {
        binding.currencyRV.layoutManager = GridLayoutManager(this@MainActivity, 2)
        binding.currencyRV.adapter = currencyAdapter
        lifecycleScope.launch {
            viewModel.result.collect {
                currencyAdapter.setData(it)
            }
        }
    }

    private fun setupErrorMessageViewer(){
        lifecycleScope.launch{
            viewModel.message.collect {
                if (it.isNotEmpty() && it.isNotBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        it,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showNoInternetToast() {
        Toast.makeText(
            this@MainActivity,
            "Internet Is not Available!",
            Toast.LENGTH_SHORT
        ).show()
    }
}