package com.codesignal.paypay.currencyconverter.useCases

import com.codesignal.paypay.currencyconverter.repository.Repository
import javax.inject.Inject

class CurrencyNameUseCase @Inject constructor(private val repository: Repository) {
    suspend fun getAllCurrencyNames() = repository.getAllCurrencyNames()
}