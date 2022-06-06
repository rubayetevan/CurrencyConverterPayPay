package com.codesignal.paypay.currencyconverter.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.codesignal.paypay.currencyconverter.common.utility.Currencies
import com.codesignal.paypay.currencyconverter.common.utility.KEY_DB_UPDATE
import com.codesignal.paypay.currencyconverter.common.utility.Resource
import com.codesignal.paypay.currencyconverter.models.LatestRates
import com.codesignal.paypay.currencyconverter.repository.local.LocalDataSource
import com.codesignal.paypay.currencyconverter.repository.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(
    private val context: Context,
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val externalScope: CoroutineScope,
    private val sharedPreferences: SharedPreferences
) {

    suspend fun getLatestRates(): Flow<Resource<LatestRates>> {
        return flow {
            emit(Resource.Loading())
            val result = withContext(externalScope.coroutineContext) {
                remoteDataSource.getLatestRates()
            }
            if (result is Resource.Success) {
                if (result.data != null) {
                    withContext(externalScope.coroutineContext) {
                        localDataSource.insertLatestRates(result.data)
                        sharedPreferences.edit().putBoolean(KEY_DB_UPDATE, true).apply()
                    }
                    emit(Resource.Success(data = result.data))

                } else {
                    emit(Resource.Empty())
                    Log.e("Repository", "rate data is empty")
                }
            } else if (result is Resource.Error) {
                result.message?.let {
                    emit(Resource.Error(message = it))
                    Log.e("Repository", it)
                }
            }
        }
    }

    suspend fun getConvertedCurrency(
        from: Currencies,
        to: Currencies,
        value: Double
    ): Flow<Resource<Double>> {
        return flow {
            emit(Resource.Loading<Double>())
            val fromValue: Double = localDataSource.getCurrencyValueBasedOnUSD(from)
            val toValue: Double = localDataSource.getCurrencyValueBasedOnUSD(to)
            var fromUsd = 1.0 / fromValue
            fromUsd *= value
            val convertedValue = toValue * fromUsd
            emit(Resource.Success<Double>(data = convertedValue))
        }
    }
}