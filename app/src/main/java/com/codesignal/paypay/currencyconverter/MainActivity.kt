package com.codesignal.paypay.currencyconverter

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.codesignal.paypay.currencyconverter.common.utility.Currencies
import com.codesignal.paypay.currencyconverter.common.utility.Resource
import com.codesignal.paypay.currencyconverter.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            val result = mainViewModel.getALatestRates()
            result.collect(){
                when(it){
                    is Resource.Loading ->{
                        Log.d("getALatestRates", "Resource.Loading")
                    }
                    is Resource.Error ->{
                        Log.d("getALatestRates", "Resource.Error")
                    }
                    is Resource.Success ->{
                        Log.d("getALatestRates", "Resource.Success")
                        Log.d("getALatestRates", it.data.toString())
                    }
                    is Resource.Empty ->{
                        Log.d("getALatestRates", "Resource.Empty")
                    }
                }
            }

            val convertedValue = mainViewModel.getCurrencyConvertedValue(Currencies.CNY,Currencies.BDT,1.0)
            convertedValue.collect(){
                when(it){
                    is Resource.Loading ->{
                        Log.d("getCurrency", "Resource.Loading")
                    }
                    is Resource.Error ->{
                        Log.d("getCurrency", "Resource.Error")
                    }
                    is Resource.Success ->{
                        Log.d("getCurrency", "Resource.Success")
                        Log.d("getCurrency", it.data.toString())
                    }
                    is Resource.Empty ->{
                        Log.d("getCurrency", "Resource.Empty")
                    }
                }
            }
        }
    }
}