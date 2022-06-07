package com.codesignal.paypay.currencyconverter.repository.remote

import com.codesignal.paypay.currencyconverter.common.api.OpenExchangeRatesApi
import com.codesignal.paypay.currencyconverter.common.utility.Resource
import com.google.gson.JsonObject
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val openExchangeRatesApi: OpenExchangeRatesApi) {

    suspend fun getLatestRates(): Resource<JsonObject?> {
        return try {
            val response = openExchangeRatesApi.getAllCurrencyRateBasedOnUSD()
            if (response.isSuccessful) {
                Resource.Success(response.body())
            } else {
                Resource.Error(response.errorBody().toString())
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Could not get data.")
        }
    }
}