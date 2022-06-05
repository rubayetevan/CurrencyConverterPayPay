package com.codesignal.paypay.currencyconverter.viewModels

import androidx.lifecycle.ViewModel
import com.codesignal.paypay.currencyconverter.common.utility.Currencies
import com.codesignal.paypay.currencyconverter.common.utility.Resource
import com.codesignal.paypay.currencyconverter.models.LatestRates
import com.codesignal.paypay.currencyconverter.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    suspend fun getALatestRates(): Flow<Resource<LatestRates>> = repository.getLatestRates()
    suspend fun getCurrencyConvertedValue(
        from: Currencies,
        to: Currencies,
        value: Double
    ): Flow<Resource<Double>> = repository.getConvertedCurrency(from, to, value)

}