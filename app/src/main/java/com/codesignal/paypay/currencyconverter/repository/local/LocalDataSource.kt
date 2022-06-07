package com.codesignal.paypay.currencyconverter.repository.local

import androidx.sqlite.db.SimpleSQLiteQuery
import com.codesignal.paypay.currencyconverter.common.utility.Currencies
import com.codesignal.paypay.currencyconverter.models.LatestRates
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val latestRatesDao: LatestRatesDao) {

    suspend fun insertLatestRates(latestRates: LatestRates){
        latestRatesDao.insertLatestRate(latestRates)
    }

    suspend fun getCurrencyValueBasedOnUSD(currency:Currencies):Double{

        val query = SimpleSQLiteQuery(
            "SELECT \"${currency.name}\" FROM LatestRates ORDER BY timestamp LIMIT 1"
        )

        return latestRatesDao.getCurrencyValueBasedOnUSD(query)

    }



}