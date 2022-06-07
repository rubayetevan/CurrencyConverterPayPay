package com.codesignal.paypay.currencyconverter.viewModels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codesignal.paypay.currencyconverter.common.utility.Currencies
import com.codesignal.paypay.currencyconverter.common.utility.KEY_DB_UPDATE
import com.codesignal.paypay.currencyconverter.common.utility.Resource
import com.codesignal.paypay.currencyconverter.models.CurrencyResult
import com.codesignal.paypay.currencyconverter.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.*
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

    var currencyValue ="10"
    fun setCurrencyValue(s: CharSequence) {
        currencyValue = s.toString()
        _result.update { "" }
    }

    init {
        val dbUpdatedTime = Date(sharedPreferences.getLong(KEY_DB_UPDATE, 0))
        val currentTime = Date(System.currentTimeMillis())

        val diff: Long = currentTime.time - dbUpdatedTime.time
        val seconds = diff / 1000
        val minutes = seconds / 60

        println("Differnce $minutes")

        if (minutes>=30) {
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

    fun getCurrencyConvertedValue(to: String):CurrencyResult {
        val _r = MutableStateFlow("")
        val r: StateFlow<String> = _r.asStateFlow()
        val currencyResult = CurrencyResult(r)
        viewModelScope.launch {
            repository.getConvertedCurrency(
                Currencies.valueOf(currencies[fromCurrencyPosition]),
                Currencies.valueOf(to),
                if(currencyValue.isBlank())0.0 else currencyValue.trim().toDouble()
            ).collect { value ->
                when (value) {
                    is Resource.Success -> {
                        value.data?.let {
                            _r.update { "${String.format("%.2f", value.data)} $to" }
                        }
                    }
                    else -> {

                    }
                }
            }
        }
        return currencyResult
    }

}