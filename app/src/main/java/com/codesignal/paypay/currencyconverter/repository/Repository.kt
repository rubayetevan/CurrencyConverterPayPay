package com.codesignal.paypay.currencyconverter.repository

import android.content.Context
import android.content.SharedPreferences
import com.codesignal.paypay.currencyconverter.common.utility.Currencies
import com.codesignal.paypay.currencyconverter.common.utility.KEY_DB_UPDATE
import com.codesignal.paypay.currencyconverter.common.utility.Resource
import com.codesignal.paypay.currencyconverter.models.CurrencyModel
import com.codesignal.paypay.currencyconverter.repository.local.LocalDataSource
import com.codesignal.paypay.currencyconverter.repository.remote.RemoteDataSource
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class Repository @Inject constructor(
    private val context: Context,
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val externalScope: CoroutineScope,
    private val sharedPreferences: SharedPreferences
) {

    private var currencies: MutableList<CurrencyModel> = ArrayList<CurrencyModel>()

    suspend fun getLatestRates(): Flow<Resource<List<CurrencyModel>>> {
        return flow {
            if (currencies.isEmpty()) {
                emit(Resource.Loading())

                val result = withContext(externalScope.coroutineContext) {
                    remoteDataSource.getLatestRates()
                }

                if (result is Resource.Success) {

                    val rates: JsonObject? = result.data?.getAsJsonObject("rates")

                    rates?.keySet()?.forEach { key ->
                        val value = rates.get(key).toString()
                        println(value)
                        currencies.add(CurrencyModel(name = key, value = value.toDouble()))
                    }

                    val data = Collections.unmodifiableList(currencies)
                    withContext(externalScope.coroutineContext) {
                        localDataSource.insertAllCurrencies(currencies = data)
                    }

                    val date = Date(System.currentTimeMillis()) //or simply new Date();
                    sharedPreferences.edit().putLong(KEY_DB_UPDATE, date.time).apply()

                    emit(Resource.Success(data = data))

                } else if (result is Resource.Error) {
                    emit(Resource.Error(result.message ?: "Could not get data."))
                }
            } else {
                val data = Collections.unmodifiableList(currencies)
                emit(Resource.Success(data = data))
            }
        }
    }

    suspend fun getConvertedCurrency(
        from: String,
        value: Double
    ): Flow<Resource<List<CurrencyModel>>> {
        return flow {
            emit(Resource.Loading<List<CurrencyModel>>())
            val results: MutableList<CurrencyModel> = ArrayList<CurrencyModel>()

            if (currencies.isEmpty()) {
                currencies = withContext(externalScope.coroutineContext) {
                    localDataSource.getAllCurrencies()
                }
            }

            val fromValue: Double? = currencies.find { it.name == from }?.value

            fromValue?.let {
                currencies.forEach { currency ->
                    val toValue: Double = currency.value
                    var fromUsd = 1.0 / fromValue
                    fromUsd *= value
                    val convertedValue = toValue * fromUsd
                    results.add(CurrencyModel(name = currency.name, value = convertedValue))
                }
                val data = Collections.unmodifiableList(results)
                if(data.isEmpty()){
                    emit(Resource.Empty())
                }else {
                    emit(Resource.Success(data = data))
                }
            } ?: run {
                emit(Resource.Error(message = "Can't convert Value"))
            }
        }
    }
}