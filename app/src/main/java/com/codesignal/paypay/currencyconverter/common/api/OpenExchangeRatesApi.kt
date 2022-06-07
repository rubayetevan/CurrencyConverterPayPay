package com.codesignal.paypay.currencyconverter.common.api

import com.codesignal.paypay.currencyconverter.common.utility.APP_ID
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenExchangeRatesApi {

    @GET("latest.json")
    suspend fun getAllCurrencyRateBasedOnUSD(
        @Query("app_id") app_id: String = APP_ID,
    ): Response<JsonObject>
}