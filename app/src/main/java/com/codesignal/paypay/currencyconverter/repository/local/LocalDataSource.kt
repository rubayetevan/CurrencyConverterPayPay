package com.codesignal.paypay.currencyconverter.repository.local

import com.codesignal.paypay.currencyconverter.models.CurrencyModel
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val currencyModelDao: CurrencyModelDao) {

    suspend fun insertAllCurrencies(currencies: List<CurrencyModel>){
        currencyModelDao.insertAll(currencies)
    }

    suspend fun getAllCurrencies():MutableList<CurrencyModel> = currencyModelDao.getAllCurrencies()

}