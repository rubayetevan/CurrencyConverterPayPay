package com.codesignal.paypay.currencyconverter.viewModels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codesignal.paypay.currencyconverter.common.utility.Currencies
import com.codesignal.paypay.currencyconverter.common.utility.KEY_DB_UPDATE
import com.codesignal.paypay.currencyconverter.common.utility.Resource
import com.codesignal.paypay.currencyconverter.models.CurrencyModel

import com.codesignal.paypay.currencyconverter.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    sharedPreferences: SharedPreferences
) : ViewModel() {

    private val a: List<CurrencyModel> = ArrayList()
    private val _result = MutableStateFlow(a)
    val result: StateFlow<List<CurrencyModel>> = _result.asStateFlow()

    private val _dbLoadingState = MutableStateFlow(true)
    val dbLoadingState: StateFlow<Boolean> = _dbLoadingState.asStateFlow()

    val currencies = Currencies.getList()
    var fromCurrencyPosition: Int = 0

    var currencyValue = "0.00"

    fun setCurrencyValue(s: CharSequence) {
        currencyValue = s.toString()
        getCurrencyConvertedValue()
    }

    init {
        val dbUpdatedTime = Date(sharedPreferences.getLong(KEY_DB_UPDATE, 0))
        val currentTime = Date(System.currentTimeMillis())

        val diff: Long = currentTime.time - dbUpdatedTime.time
        val seconds = diff / 1000
        val minutes = seconds / 60

        println("Differnce $minutes")

        if (minutes >= 30) {
            viewModelScope.launch {
                _dbLoadingState.update { true }
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
        } else {
            _dbLoadingState.update { false }
        }
    }

    fun getCurrencyConvertedValue() {
        viewModelScope.launch {
            val convertedCurrencies = repository.getConvertedCurrency(
                currencies[fromCurrencyPosition],
                currencyValue.trim().toDouble()
            )
            convertedCurrencies.collect { d ->
                when (d) {
                    is Resource.Success -> {
                        d.data?.let {
                            _result.update { d.data }
                        }
                    }
                    else -> {}
                }
            }

        }
    }

}