package com.codesignal.paypay.currencyconverter.viewModels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codesignal.paypay.currencyconverter.common.utility.Currencies
import com.codesignal.paypay.currencyconverter.common.utility.KEY_DB_UPDATE
import com.codesignal.paypay.currencyconverter.common.utility.Resource
import com.codesignal.paypay.currencyconverter.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _result = MutableStateFlow(String())
    val result: StateFlow<String> = _result.asStateFlow()

    private val _dbLoadingState = MutableStateFlow(true)
    val dbLoadingState: StateFlow<Boolean> = _dbLoadingState.asStateFlow()

    val currencies = Currencies.getList()
    var fromCurrencyPosition: Int = 0

    var valueInString = ""

    init {
        val dbUpdated = sharedPreferences.getBoolean(KEY_DB_UPDATE, false)
        if (!dbUpdated) {
            viewModelScope.launch {
                repository.getLatestRates().collect { value ->
                    when (value) {
                        is Resource.Success -> {
                            _dbLoadingState.update { false }
                        }
                        is Resource.Error -> {
                            _dbLoadingState.update { false }
                        }
                        is Resource.Loading -> {
                            _dbLoadingState.update { true }
                        }
                        is Resource.Empty -> {
                            _dbLoadingState.update { false }
                        }
                    }
                }
            }
        }else{
            _dbLoadingState.update { false }
        }
    }

     fun getCurrencyConvertedValue() {
         viewModelScope.launch {
             repository.getConvertedCurrency(
                 Currencies.valueOf(currencies[fromCurrencyPosition]),
                 Currencies.BDT,
                 valueInString.trim().toDouble()
             ).collect { value ->
                 when (value) {
                     is Resource.Success -> {
                         value.data?.let {
                             _result.update { String.format("%.2f", value.data) }
                         }
                     }
                     else -> {

                     }
                 }
             }
         }
    }

}