package com.codesignal.paypay.currencyconverter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codesignal.paypay.currencyconverter.common.utility.Resource
import com.codesignal.paypay.currencyconverter.common.utility.Validators
import com.codesignal.paypay.currencyconverter.models.CurrencyModel
import com.codesignal.paypay.currencyconverter.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val validators: Validators,
) : ViewModel() {

    private val _result = MutableStateFlow(emptyList<CurrencyModel>())
    val result: StateFlow<List<CurrencyModel>> = _result.asStateFlow()

    private val _currencyNames = MutableStateFlow(emptyList<String>())
    val currencyNames: StateFlow<List<String>> = _currencyNames.asStateFlow()

    private val _dbLoadingState = MutableStateFlow(true)
    val dbLoadingState: StateFlow<Boolean> = _dbLoadingState.asStateFlow()

    private val _internetState = MutableSharedFlow<Boolean>(replay = 0)
    val internetState: SharedFlow<Boolean> = _internetState.asSharedFlow()

    var fromCurrencyPosition: Int = 0
    var currencyValue = "0.00"

    fun setCurrencyValue(s: CharSequence) {
        val value = s.toString()
        currencyValue = if(validators.validateDecimalInput(value)) value else "0.00"
        viewModelScope.launch {
            getCurrencyConvertedValue()
        }
    }

    init {
        viewModelScope.launch {
            internetState.collect {
                println("internetState.collect $it")
                if (it) {
                    if (repository.shouldUpdateDB()) {
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
        if (repository.getDbInitializationState()) {
            val value: Double = if (currencyValue.isBlank() || currencyValue.isEmpty()) 0.00
            else currencyValue.trim().toDouble()
            if(currencyNames.value.isNotEmpty()) {
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
    }

    private fun getCurrencyValues() {
        if (repository.getDbInitializationState()) {
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
        viewModelScope.launch {
            _internetState.emit(b)
        }
    }


}