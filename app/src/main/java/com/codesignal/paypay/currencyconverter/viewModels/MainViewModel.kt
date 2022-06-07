package com.codesignal.paypay.currencyconverter.viewModels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codesignal.paypay.currencyconverter.common.utility.DB_UPDATE_TH_MIN
import com.codesignal.paypay.currencyconverter.common.utility.KEY_DB_UPDATE
import com.codesignal.paypay.currencyconverter.common.utility.KEY_DB_UPDATE_TIME
import com.codesignal.paypay.currencyconverter.common.utility.Resource
import com.codesignal.paypay.currencyconverter.models.CurrencyModel
import com.codesignal.paypay.currencyconverter.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val initialResult: List<CurrencyModel> = ArrayList()
    private val initialCurrencyNames: List<String> = ArrayList()

    private val _result = MutableStateFlow(initialResult)
    val result: StateFlow<List<CurrencyModel>> = _result.asStateFlow()

    private val _currencyNames = MutableStateFlow(initialCurrencyNames)
    val currencyNames: StateFlow<List<String>> = _currencyNames.asStateFlow()

    private val _dbLoadingState = MutableStateFlow(true)
    val dbLoadingState: StateFlow<Boolean> = _dbLoadingState.asStateFlow()

    private val _internetState = MutableStateFlow(true)
    private val internetState: StateFlow<Boolean> = _internetState.asStateFlow()


    var fromCurrencyPosition: Int = 0
    var currencyValue = "0.00"

    fun setCurrencyValue(s: CharSequence) {
        currencyValue = s.toString()
        if (currencyValue.isEmpty() || currencyValue.isBlank()) {
            currencyValue = "0"
        }
        viewModelScope.launch {
            getCurrencyConvertedValue()
        }
    }

    init {
        viewModelScope.launch {
            internetState.collect {
                println("internetState.collect $it")
                if (it) {
                    val dbUpdatedTime = Date(sharedPreferences.getLong(KEY_DB_UPDATE_TIME, 0))
                    val currentTime = Date(System.currentTimeMillis())

                    val diff: Long = currentTime.time - dbUpdatedTime.time
                    val seconds = diff / 1000
                    val minutes = seconds / 60

                    if (minutes >= DB_UPDATE_TH_MIN) {
                        _dbLoadingState.update { true }
                        repository.getLatestRates().collect { value ->
                            when (value) {
                                is Resource.Success -> {
                                    _dbLoadingState.update { false }
                                    getCurrencyValues()
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

                    } else {
                        _dbLoadingState.update { false }
                    }
                }
            }
        }

        if (!dbLoadingState.value) {
            getCurrencyValues()
        }
    }

    fun getCurrencyConvertedValue() {
        if (sharedPreferences.getBoolean(KEY_DB_UPDATE, false)) {
            val value: Double = if (currencyValue.isBlank() || currencyValue.isEmpty()) 0.00
            else currencyValue.trim().toDouble()
            viewModelScope.launch {
                val convertedValue = repository.getConvertedCurrency(
                    currencyNames.value[fromCurrencyPosition],
                    value
                )
                convertedValue.collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _result.update { resource.data!! }
                        }
                        is Resource.Error -> {

                        }
                        is Resource.Loading -> {

                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }

    private fun getCurrencyValues() {
        if (sharedPreferences.getBoolean(KEY_DB_UPDATE, false)) {
            viewModelScope.launch {
                repository.getAllCurrencyNames().collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _currencyNames.update { resource.data!! }
                        }
                        is Resource.Error -> {

                        }
                        is Resource.Loading -> {

                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }

    fun hasInternet(b: Boolean) {
        _internetState.update { b }
    }


}