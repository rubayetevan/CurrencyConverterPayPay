package com.codesignal.paypay.currencyconverter.repository

import android.content.Context
import com.codesignal.paypay.currencyconverter.common.utility.Currencies
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
    private val externalScope: CoroutineScope
) {

    suspend fun getLatestRates(): Flow<Resource<LatestRates>> {
        return flow {
            emit(Resource.Loading<LatestRates>())

            val result = withContext(externalScope.coroutineContext) {
                remoteDataSource.getLatestRates()
            }

            if (result is Resource.Success) {
                if (result.data != null) {
                    emit(Resource.Success<LatestRates>(result.data))
                    withContext(externalScope.coroutineContext) {
                        localDataSource.insertLatestRates(result.data)
                    }
                } else {
                    emit(Resource.Empty<LatestRates>())
                }
            } else if (result is Resource.Error) {
                emit(Resource.Error<LatestRates>(result.message ?: "Error"))
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
            val fromValue = localDataSource.getCurrencyValueBasedOnUSD(from)
            val toValue = localDataSource.getCurrencyValueBasedOnUSD(to)
            if (fromValue != null && toValue != null) {
                var fromUsd = 1.0 / fromValue
                fromUsd *= value
                val convertedValue = toValue * fromUsd
                emit(Resource.Success<Double>(data = convertedValue))
            } else {
                emit(Resource.Error<Double>(data = null, message = "Failed to convert currency"))
            }

        }
    }
}